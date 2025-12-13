package com.noveletytech.examsphere.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.Question
import com.noveletytech.examsphere.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<Int, String> = emptyMap(),
    val score: Int = 0,
    val isLoading: Boolean = true
)

class QuizViewModel(private val categoryId: String, private val subCategoryId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            val mcqs = repository.getMcqQuestions(categoryId, subCategoryId)
            val saqs = repository.getSaqQuestions(categoryId, subCategoryId)
            val laqs = repository.getLaqQuestions(categoryId, subCategoryId)
            _uiState.value = _uiState.value.copy(questions = mcqs + saqs + laqs, isLoading = false)
        }
    }

    fun onAnswerSelected(questionIndex: Int, answer: String) {
        val newAnswers = _uiState.value.selectedAnswers.toMutableMap()
        newAnswers[questionIndex] = answer
        _uiState.value = _uiState.value.copy(selectedAnswers = newAnswers)
    }

    fun onNextClicked() {
        if (_uiState.value.currentQuestionIndex < _uiState.value.questions.size - 1) {
            _uiState.value = _uiState.value.copy(currentQuestionIndex = _uiState.value.currentQuestionIndex + 1)
        }
    }

    fun onPreviousClicked() {
        if (_uiState.value.currentQuestionIndex > 0) {
            _uiState.value = _uiState.value.copy(currentQuestionIndex = _uiState.value.currentQuestionIndex - 1)
        }
    }

    fun calculateScore(): Int {
        var score = 0
        val questions = _uiState.value.questions
        val selectedAnswers = _uiState.value.selectedAnswers

        for (i in questions.indices) {
            val question = questions[i]
            val correctAnswer = when (question) {
                is Question.MCQ -> question.correctAnswer
                is Question.SAQ -> question.correctAnswer
                is Question.LAQ -> "" // LAQs are not auto-scored
            }
            if (selectedAnswers[i] == correctAnswer) {
                score++
            }
        }
        _uiState.value = _uiState.value.copy(score = score)
        return score
    }
}