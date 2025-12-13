package com.noveletytech.examsphere.auth

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.noveletytech.examsphere.auth.composables.SocialMediaButtons
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignUpClicked: () -> Unit) {
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var email by remember { mutableStateOf("joedoe75@gmail.com") }
    var password by remember { mutableStateOf("........") }

    LaunchedEffect(uiState.signInSuccess) {
        if (uiState.signInSuccess) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Exam Sphere", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { viewModel.signIn(email, password) }) {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        SocialMediaButtons(onGoogleClick = {
            coroutineScope.launch {
                try {
                    val credentialManager = CredentialManager.create(context)
                    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId("734617860228-enqf76kgjveqsdfmfbls9n2djv6aja3f.apps.googleusercontent.com")
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(context, request)
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    viewModel.signInWithGoogle(idToken)
                } catch (e: GetCredentialException) {
                    Log.e("LoginScreen", "Google Sign-In GetCredentialException: ${e.message}", e)
                } catch (e: Exception) {
                    Log.e("LoginScreen", "Google Sign-In failed with unexpected exception: ${e.message}", e)
                }
            }
        })

        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onSignUpClicked) {
            Text("Don\'t have an account? Sign Up")
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}