package com.noveletytech.examsphere.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // ... (existing functions)

    suspend fun addCategory(category: Category) {
        val document = firestore.collection("categories").document()
        category.copy(key = document.id).also { 
            document.set(it).await()
        }
    }

    suspend fun updateCategory(category: Category) {
        firestore.collection("categories").document(category.key).set(category).await()
    }

    suspend fun deleteCategory(categoryId: String) {
        firestore.collection("categories").document(categoryId).delete().await()
    }

    suspend fun addSubCategory(categoryId: String, subCategory: SubCategory) {
        val document = firestore.collection("categories").document(categoryId)
            .collection("subCategories").document()
        subCategory.copy(key = document.id).also {
            document.set(it).await()
        }
    }

    suspend fun addMcqQuestion(categoryId: String, subCategoryId: String, question: Question.MCQ) {
        val document = firestore.collection("categories").document(categoryId)
            .collection("subCategories").document(subCategoryId)
            .collection("mcqQuestions").document()
        question.copy(id = document.id).also {
            document.set(it).await()
        }
    }

    suspend fun addSaqQuestion(categoryId: String, subCategoryId: String, question: Question.SAQ) {
        val document = firestore.collection("categories").document(categoryId)
            .collection("subCategories").document(subCategoryId)
            .collection("saqQuestions").document()
        question.copy(id = document.id).also {
            document.set(it).await()
        }
    }

    suspend fun addLaqQuestion(categoryId: String, subCategoryId: String, question: Question.LAQ) {
        val document = firestore.collection("categories").document(categoryId)
            .collection("subCategories").document(subCategoryId)
            .collection("laqQuestions").document()
        question.copy(id = document.id).also {
            document.set(it).await()
        }
    }
    suspend fun getCategories(): List<Category> {
        return try {
            firestore.collection("categories")
                .get()
                .await()
                .toObjects(Category::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getSubCategories(categoryId: String): List<SubCategory> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("subCategories")
                .get()
                .await()
                .toObjects(SubCategory::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getMcqQuestions(categoryId: String, subCategoryId: String): List<Question.MCQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("subCategories").document(subCategoryId)
                .collection("mcqQuestions")
                .get()
                .await()
                .toObjects(Question.MCQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getSaqQuestions(categoryId: String, subCategoryId: String): List<Question.SAQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("subCategories").document(subCategoryId)
                .collection("saqQuestions")
                .get()
                .await()
                .toObjects(Question.SAQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getLaqQuestions(categoryId: String, subCategoryId: String): List<Question.LAQ> {
        return try {
            firestore.collection("categories").document(categoryId)
                .collection("subCategories").document(subCategoryId)
                .collection("laqQuestions")
                .get()
                .await()
                .toObjects(Question.LAQ::class.java)
        } catch (_: Exception) {
            emptyList()
        }
    }
}
