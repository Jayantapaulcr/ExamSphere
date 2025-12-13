package com.noveletytech.examsphere.data

data class Category(
    val key: String = "", 
    val name: String = "", 
    val imageUrl: String = ""
)

data class Branch(
    val key: String = "",
    val name: String = "",
    val imageUrl: String = ""
)

data class Subject(
    val key: String = "",
    val name: String = ""
)

data class Chapter(
    val key: String = "",
    val name: String = ""
)

data class RecentActivity(
    val key: String, 
    val name: String, 
    val imageUrl: String, 
    val questions: Int, 
    val score: Int, 
    val total: Int
)

sealed class Question(
    open val id: String = "", 
    open val text: String = "", 
    open val index: Int = 0,
    open val chapterName: String = "",
    open val chapterIndex: Int = 0
) {
    data class MCQ(
        override val id: String = "",
        override val index: Int = 0,
        override val chapterName: String = "",
        override val chapterIndex: Int = 0,
        override val text: String = "",
        val options: List<String> = emptyList(),
        val correctAnswer: String = ""
    ) : Question(id, text, index, chapterName, chapterIndex)

    data class SAQ(
        override val id: String = "",
        override val index: Int = 0,
        override val chapterName: String = "",
        override val chapterIndex: Int = 0,
        override val text: String = "",
        val correctAnswer: String = "" 
    ) : Question(id, text, index, chapterName, chapterIndex)

    data class LAQ(
        override val id: String = "",
        override val index: Int = 0,
        override val chapterName: String = "",
        override val chapterIndex: Int = 0,
        override val text: String = ""
    ) : Question(id, text, index, chapterName, chapterIndex)
}

data class User(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val pinCode: String = "",
    val state: String = "",
    val nation: String = "",
    val profilePictureUrl: String = ""
)

data class AppConfig(
    val maintenanceMode: Boolean = false,
    val showCarousel: Boolean = true,
    val primaryColor: String = "#1A3B8E",
    val dailyUpdate: String = "",
    val animationSpeed: Long = 9000,
    val transitionSpeed: Long = 400
)
