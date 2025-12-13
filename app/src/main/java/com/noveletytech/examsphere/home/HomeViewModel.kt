package com.noveletytech.examsphere.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Category(val key: String, val name: String, val icon: ImageVector)
data class RecentActivity(val key: String, val name: String, val questions: Int, val score: Int, val total: Int)

class HomeViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities

    init {
        // Initialize with some sample data
        _categories.value = listOf(
            Category("html_key", "HTML", Icons.Filled.Star),
            Category("js_key", "Javascript", Icons.Filled.Star),
            Category("react_key", "React", Icons.Filled.Star),
            Category("cpp_key", "C++", Icons.Filled.Star),
            Category("python_key", "Python", Icons.Filled.Star)
        )

        _recentActivities.value = listOf(
            RecentActivity("html_activity_key", "HTML", 30, 26, 30),
            RecentActivity("js_activity_key", "Javascript", 30, 25, 30)
        )
    }

    fun onCategoryClicked(category: Category) {
        val newActivity = RecentActivity("${category.key}_activity", category.name, 30, 0, 30) // Assuming 30 questions and initial score of 0
        val currentActivities = _recentActivities.value.toMutableList()

        // Add the new activity to the top and remove duplicates
        currentActivities.removeAll { it.name == newActivity.name }
        currentActivities.add(0, newActivity)

        _recentActivities.value = currentActivities
    }
}