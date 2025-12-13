package com.noveletytech.examsphere.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.QuizRepository
import com.noveletytech.examsphere.data.SubCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubCategoryViewModel(private val categoryId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _subCategories = MutableStateFlow<List<SubCategory>>(emptyList())
    val subCategories: StateFlow<List<SubCategory>> = _subCategories

    init {
        fetchSubCategories()
    }

    private fun fetchSubCategories() {
        viewModelScope.launch {
            _subCategories.value = repository.getSubCategories(categoryId)
        }
    }
}

class SubCategoryViewModelFactory(private val categoryId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SubCategoryViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}