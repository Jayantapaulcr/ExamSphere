package com.noveletytech.examsphere.branches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class BranchesViewModelFactory(private val categoryId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BranchesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BranchesViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}