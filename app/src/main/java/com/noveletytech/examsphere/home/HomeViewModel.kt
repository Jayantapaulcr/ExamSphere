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
        // You can also fetch recent activities here if they are stored in Firestore
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
        }
    }

    fun onCategoryClicked(category: Category) {
        val newActivity = RecentActivity("${category.key}_activity", category.name, category.imageUrl, 30, 0, 30)
        val currentActivities = _recentActivities.value.toMutableList()

        currentActivities.removeAll { it.name == newActivity.name }
        currentActivities.add(0, newActivity)

        _recentActivities.value = currentActivities
    }
}