package com.noveletytech.examsphere.data

data class Category(
    val key: String = "", 
    val name: String = "", 
    val imageUrl: String = ""
)

data class SubCategory(
    val key: String = "",
    val name: String = "",
    val mcqCount: Int = 0,
    val saqCount: Int = 0,
    val laqCount: Int = 0
)

data class RecentActivity(
    val key: String, 
    val name: String, 
    val imageUrl: String, 
    val questions: Int, 
    val score: Int, 
    val total: Int
)

sealed class Question(open val id: String = "", open val text: String = "") {
    data class MCQ(
        override val id: String = "",
        override val text: String = "",
        val options: List<String> = emptyList(),
        val correctAnswer: String = ""
    ) : Question(id, text)

    data class SAQ(
        override val id: String = "",
        override val text: String = "",
        val correctAnswer: String = "" 
    ) : Question(id, text)

    data class LAQ(
        override val id: String = "",
        override val text: String = ""
        // No correct answer for long-answer questions
    ) : Question(id, text)
}