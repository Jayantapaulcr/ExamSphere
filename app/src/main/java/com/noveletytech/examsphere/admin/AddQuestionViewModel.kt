package com.noveletytech.examsphere.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.Question
import com.noveletytech.examsphere.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AddQuestionUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

class AddQuestionViewModel(
    private val categoryId: String,
    private val subCategoryId: String
) : ViewModel() {

    private val repository = QuizRepository()

    private val _uiState = MutableStateFlow(AddQuestionUiState())
    val uiState: StateFlow<AddQuestionUiState> = _uiState

    fun addMcqQuestion(text: String, options: List<String>, correctAnswer: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val question = Question.MCQ(text = text, options = options, correctAnswer = correctAnswer)
                repository.addMcqQuestion(categoryId, subCategoryId, question)
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun addSaqQuestion(text: String, correctAnswer: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val question = Question.SAQ(text = text, correctAnswer = correctAnswer)
                repository.addSaqQuestion(categoryId, subCategoryId, question)
                _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}

class AddQuestionViewModelFactory(
    private val categoryId: String,
    private val subCategoryId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddQuestionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddQuestionViewModel(categoryId, subCategoryId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}