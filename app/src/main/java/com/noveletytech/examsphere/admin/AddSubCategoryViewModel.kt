package com.noveletytech.examsphere.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.QuizRepository
import com.noveletytech.examsphere.data.SubCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AddSubCategoryUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AddSubCategoryViewModel(private val categoryId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _uiState = MutableStateFlow(AddSubCategoryUiState())
    val uiState: StateFlow<AddSubCategoryUiState> = _uiState

    fun addSubCategory(name: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                repository.addSubCategory(categoryId, SubCategory(name = name))
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

class AddSubCategoryViewModelFactory(private val categoryId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSubCategoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddSubCategoryViewModel(categoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}