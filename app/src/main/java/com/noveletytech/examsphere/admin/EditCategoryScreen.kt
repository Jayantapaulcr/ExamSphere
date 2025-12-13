package com.noveletytech.examsphere.admin

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun EditCategoryScreen(categoryId: String, onCategoryUpdated: () -> Unit) {
    val viewModel: EditCategoryViewModel = viewModel(factory = EditCategoryViewModelFactory(categoryId))
    val uiState by viewModel.uiState.collectAsState()

    var name by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    LaunchedEffect(uiState.category) {
        uiState.category?.let {
            name = it.name
            imageUrl = it.imageUrl
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onCategoryUpdated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Edit Category", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Category Name") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = imageUrl,
            onValueChange = { imageUrl = it },
            label = { Text("Image URL") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = { viewModel.updateCategory(name, imageUrl) }) {
                Text("Save")
            }
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}