package com.noveletytech.examsphere.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.AuthRepository
import com.noveletytech.examsphere.data.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AdminSignUpUiState(
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
    val error: String? = null
)

class AdminSignUpViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(AdminSignUpUiState())
    val uiState: StateFlow<AdminSignUpUiState> = _uiState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signUp(email, password)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, signUpSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }
}