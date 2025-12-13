package com.noveletytech.examsphere.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noveletytech.examsphere.data.AuthRepository
import com.noveletytech.examsphere.data.AuthResult
import com.noveletytech.examsphere.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SignUpUiState(
    val isLoading: Boolean = false,
    val signUpSuccess: Boolean = false,
    val error: String? = null
)

class SignUpViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState

    fun signUp(user: User, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signUp(user, password)) {
                is AuthResult.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, signUpSuccess = true)
                }
                is AuthResult.Error -> {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = result.message)
                }
            }
        }
    }

    fun signUpWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            when (val result = authRepository.signInWithGoogle(idToken)) {
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