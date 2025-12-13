package com.noveletytech.examsphere.subcategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.QuizRepository
import com.noveletytech.examsphere.data.Subject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectsViewModel(private val categoryId: String, private val branchId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _subjects = MutableStateFlow<List<Subject>>(emptyList())
    val subjects: StateFlow<List<Subject>> = _subjects

    init {
        fetchSubjects()
    }

    private fun fetchSubjects() {
        viewModelScope.launch {
            _subjects.value = repository.getSubjects(categoryId, branchId)
        }
    }
}