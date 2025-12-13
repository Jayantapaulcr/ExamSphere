package com.noveletytech.examsphere.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_INVALID_EMAIL" -> "Invalid email format."
                "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again."
                "ERROR_USER_NOT_FOUND" -> "No account found with this email."
                else -> "An unknown error occurred."
            }
            AuthResult.Error(errorMessage)
        } catch (e: Exception) {
            AuthResult.Error("An unexpected error occurred. Please check your network connection.")
        }
    }

    suspend fun signUp(user: User, password: String): AuthResult {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(user.email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                firestore.collection("users").document(firebaseUser.uid).set(user.copy(uid = firebaseUser.uid)).await()
            }
            AuthResult.Success
        } catch (e: FirebaseAuthException) {
            val errorMessage = when (e.errorCode) {
                "ERROR_EMAIL_ALREADY_IN_USE" -> "This email is already in use by another account."
                "ERROR_WEAK_PASSWORD" -> "The password is too weak."
                else -> "An unknown error occurred during sign-up."
            }
            AuthResult.Error(errorMessage)
        } catch (e: Exception) {
            AuthResult.Error("An unexpected error occurred. Please try again.")
        }
    }

    suspend fun signInWithGoogle(idToken: String): AuthResult {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = firebaseAuth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                // Always update the profile picture and names to keep them in sync with Google
                val userData = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    firstName = firebaseUser.displayName?.split(" ")?.firstOrNull() ?: "",
                    lastName = firebaseUser.displayName?.split(" ")?.lastOrNull() ?: "",
                    profilePictureUrl = firebaseUser.photoUrl?.toString() ?: ""
                )
                // Use .set with SetOptions.merge() to only update fields that changed or add new ones
                firestore.collection("users").document(firebaseUser.uid)
                    .set(userData, com.google.firebase.firestore.SetOptions.merge()).await()
            }
            AuthResult.Success
        } catch (e: Exception) {
            AuthResult.Error("Google Sign-In failed: ${e.localizedMessage}")
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser ?: return null
        return try {
            firestore.collection("users").document(firebaseUser.uid).get().await().toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun isUserAdmin(): Boolean {
        val firebaseUser = firebaseAuth.currentUser ?: return false
        return try {
            val document = firestore.collection("admins").document(firebaseUser.uid).get().await()
            document.exists()
        } catch (e: Exception) {
            false
        }
    }
}
