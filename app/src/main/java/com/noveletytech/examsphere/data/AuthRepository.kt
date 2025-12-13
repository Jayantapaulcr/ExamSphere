package com.noveletytech.examsphere.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.tasks.await

sealed class AuthResult {
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}

class AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()

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

    suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email, password).await()
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

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun isUserAdmin(): Boolean {
        return firebaseAuth.currentUser != null
    }
}
