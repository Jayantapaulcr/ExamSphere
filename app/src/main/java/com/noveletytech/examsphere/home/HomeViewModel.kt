package com.noveletytech.examsphere.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.Category
import com.noveletytech.examsphere.data.QuizRepository
import com.noveletytech.examsphere.data.RecentActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = QuizRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun addCategoryToRecents(category: Category) {
        val newActivity = RecentActivity(
            key = "${category.key}_activity",
            name = category.name,
            imageUrl = category.imageUrl,
            questions = 0, // This should be updated after a quiz is taken
            score = 0, 
            total = 0
        )

        val currentActivities = _recentActivities.value.toMutableList()

        // Remove any existing activity with the same name and add the new one to the top
        currentActivities.removeAll { it.name == newActivity.name }
        currentActivities.add(0, newActivity)

        _recentActivities.value = currentActivities
    }
}