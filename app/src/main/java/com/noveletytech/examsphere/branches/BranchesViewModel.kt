package com.noveletytech.examsphere.branches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.Branch
import com.noveletytech.examsphere.data.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BranchesViewModel(private val categoryId: String) : ViewModel() {

    private val repository = QuizRepository()

    private val _branches = MutableStateFlow<List<Branch>>(emptyList())
    val branches: StateFlow<List<Branch>> = _branches

    init {
        fetchBranches()
    }

    private fun fetchBranches() {
        viewModelScope.launch {
            _branches.value = repository.getBranches(categoryId)
        }
    }
}