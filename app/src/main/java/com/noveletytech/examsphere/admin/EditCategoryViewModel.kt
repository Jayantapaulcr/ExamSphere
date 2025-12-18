package com.noveletytech.examsphere.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.Category
import com.noveletytech.examsphere.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditCategoryUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val category: Category? = null
)

class EditCategoryViewModel(private val categoryId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _uiState = MutableStateFlow(EditCategoryUiState())
    val uiState: StateFlow<EditCategoryUiState> = _uiState

    init {
        fetchCategory()
    }

    private fun fetchCategory() {
        viewModelScope.launch {
            // In a real app, you'd fetch a single category from the repository
            val category = repository.getCategories().find { it.key == categoryId }
            _uiState.value = _uiState.value.copy(category = category)
        }
    }

    fun updateCategory(name: String, imageUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val updatedCategory = _uiState.value.category?.copy(name = name, imageUrl = imageUrl)
                if (updatedCategory != null) {
                    repository.updateCategory(updatedCategory)
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

class EditCategoryViewModelFactory(private val categoryId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditCategoryViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}