package com.noveletytech.examsphere.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun getCategories(): List<Category> {
        return try {
            firestore.collection("categories")
                .get()
                .await()
                .toObjects(Category::class.java)
        } catch (_: Exception) {
            // Handle exceptions, e.g., network errors
            emptyList()
        }
    }

    suspend fun getQuestions(categoryId: String): List<Question> {
        return try {
            firestore.collection("categories")
                .document(categoryId)
                .collection("questions")
                .get()
                .await()
                .toObjects(Question::class.java)
        } catch (_: Exception) {
            // Handle exceptions, e.g., network errors
            emptyList()
        }
    }
}
