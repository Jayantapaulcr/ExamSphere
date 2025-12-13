package com.noveletytech.examsphere.categories

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CategoryItemData(val key: String, val name: String, val icon: ImageVector, val questions: Int)

class CategoriesViewModel : ViewModel() {

    private val _categories = MutableStateFlow<List<CategoryItemData>>(emptyList())
    val categories: StateFlow<List<CategoryItemData>> = _categories

    init {
        // Initialize with some sample data
        _categories.value = listOf(
            CategoryItemData("html_key", "HTML", Icons.Filled.Star, 30),
            CategoryItemData("js_key", "Javascript", Icons.Filled.Star, 30),
            CategoryItemData("react_key", "React", Icons.Filled.Star, 30),
            CategoryItemData("cpp_key", "C++", Icons.Filled.Star, 30),
            CategoryItemData("python_key", "Python", Icons.Filled.Star, 30)
        )
    }
}