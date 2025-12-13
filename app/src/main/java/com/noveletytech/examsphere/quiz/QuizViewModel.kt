package com.noveletytech.examsphere.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

class QuizViewModel(
    private val categoryId: String, 
    private val branchId: String, 
    private val subjectId: String,
    private val questionType: String
) : ViewModel() {

    private val repository = QuizRepository()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    init {
        fetchQuestions()
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            val questions = when (questionType) {
                "mcq" -> repository.getMcqQuestions(categoryId, branchId, subjectId)
                "saq" -> repository.getSaqQuestions(categoryId, branchId, subjectId)
                "laq" -> repository.getLaqQuestions(categoryId, branchId, subjectId)
                else -> emptyList()
            }
            _uiState.value = _uiState.value.copy(questions = questions, isLoading = false)
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
            val userAnswer = selectedAnswers[i]

            if (userAnswer != null) {
                when (question) {
                    is Question.MCQ -> {
                        if (userAnswer == question.correctAnswer) {
                            score++
                        }
                    }
                    is Question.SAQ -> {
                        if (userAnswer.equals(question.correctAnswer, ignoreCase = true)) {
                            score++
                        }
                    }
                    is Question.LAQ -> {
                        // Long answer questions are not auto-scored
                    }
                }
            }
        }
        _uiState.value = _uiState.value.copy(score = score)
        return score
    }
}

class QuizViewModelFactory(
    private val categoryId: String,
    private val branchId: String,
    private val subjectId: String,
    private val questionType: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuizViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizViewModel(categoryId, branchId, subjectId, questionType) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}