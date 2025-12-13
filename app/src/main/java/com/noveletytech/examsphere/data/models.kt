package com.noveletytech.examsphere.data

data class Category(val key: String = "", val name: String = "", val imageUrl: String = "", val questions: Int = 0)
data class RecentActivity(val key: String, val name: String, val imageUrl: String, val questions: Int, val score: Int, val total: Int)
data class Question(
    val question: String = "",
    val options: List<String> = emptyList(),
    val answer: String = ""
)
