package com.noveletytech.examsphere.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.AppConfig
import com.noveletytech.examsphere.data.AuthRepository
import com.noveletytech.examsphere.data.Category
import com.noveletytech.examsphere.data.QuizRepository
import com.noveletytech.examsphere.data.RecentActivity
import com.noveletytech.examsphere.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val quizRepository = QuizRepository()
    private val authRepository = AuthRepository()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _recentActivities = MutableStateFlow<List<RecentActivity>>(emptyList())
    val recentActivities: StateFlow<List<RecentActivity>> = _recentActivities

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _updateImageUrls = MutableStateFlow<List<String>>(emptyList())
    val updateImageUrls: StateFlow<List<String>> = _updateImageUrls

    private val _appConfig = MutableStateFlow(AppConfig())
    val appConfig: StateFlow<AppConfig> = _appConfig

    init {
        fetchCategories()
        fetchCurrentUser()
        listenToCarouselUpdates()
        listenToAppConfig()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            _categories.value = quizRepository.getCategories()
        }
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch {
            _currentUser.value = authRepository.getCurrentUser()
        }
    }

    private fun listenToCarouselUpdates() {
        viewModelScope.launch {
            quizRepository.getCarouselUrls().collectLatest { urls ->
                _updateImageUrls.value = urls
            }
        }
    }

    private fun listenToAppConfig() {
        viewModelScope.launch {
            quizRepository.getAppConfig().collectLatest { config ->
                _appConfig.value = config
            }
        }
    }

    fun addCategoryToRecents(category: Category) {
        viewModelScope.launch {
            val newActivity = RecentActivity(
                key = "${category.key}_activity",
                name = category.name,
                imageUrl = category.imageUrl,
                questions = 0, 
                score = 0, 
                total = 0
            )

            val currentActivities = _recentActivities.value.toMutableList()
            currentActivities.removeAll { it.name == newActivity.name }
            currentActivities.add(0, newActivity)

            _recentActivities.value = currentActivities
        }
    }

    fun clearRecentActivities() {
        _recentActivities.value = emptyList()
    }
}