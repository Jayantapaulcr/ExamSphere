package com.noveletytech.examsphere.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getAppConfig(): Flow<AppConfig> = callbackFlow {
        val listener = firestore.collection("app_settings").document("config")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val config = snapshot?.toObject(AppConfig::class.java) ?: AppConfig()
                trySend(config)
            }
        awaitClose { listener.remove() }
    }

    fun getCarouselUrls(): Flow<List<String>> = callbackFlow {
        val listener = firestore.collection("updates").document("image_carousel")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val urls = snapshot?.get("urls") as? List<String> ?: emptyList()
                trySend(urls)
            }
        awaitClose { listener.remove() }
    }

    suspend fun getCategories(): List<Category> {
        return try {
            firestore.collection("categories").get().await().toObjects(Category::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getBranches(categoryId: String): List<Branch> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("branches")
                .get()
                .await()
                .toObjects(Branch::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getSubjects(categoryId: String, branchId: String): List<Subject> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("branches").document(branchId)
                .collection("subjects")
                .get()
                .await()
                .toObjects(Subject::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getMcqQuestions(categoryId: String, branchId: String, subjectId: String): List<Question.MCQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("branches").document(branchId)
                .collection("subjects").document(subjectId)
                .collection("mcqQuestions")
                .orderBy("index", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Question.MCQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getSaqQuestions(categoryId: String, branchId: String, subjectId: String): List<Question.SAQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("branches").document(branchId)
                .collection("subjects").document(subjectId)
                .collection("saqQuestions")
                .orderBy("index", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Question.SAQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getLaqQuestions(categoryId: String, branchId: String, subjectId: String): List<Question.LAQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("branches").document(branchId)
                .collection("subjects").document(subjectId)
                .collection("laqQuestions")
                .orderBy("index", Query.Direction.ASCENDING)
                .get()
                .await()
                .toObjects(Question.LAQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
