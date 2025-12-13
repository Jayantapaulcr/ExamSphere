package com.noveletytech.examsphere.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SubjectsViewModelFactory(private val categoryId: String, private val branchId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubjectsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubjectsViewModel(categoryId, branchId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}