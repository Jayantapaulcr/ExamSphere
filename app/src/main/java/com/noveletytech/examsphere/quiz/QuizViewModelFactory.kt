package com.noveletytech.examsphere.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class QuizViewModelFactory(private val categoryId: String, private val subCategoryId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(categoryId, subCategoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}