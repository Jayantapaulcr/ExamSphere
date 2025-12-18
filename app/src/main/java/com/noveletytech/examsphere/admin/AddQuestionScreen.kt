package com.noveletytech.examsphere.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
fun AddQuestionScreen(
    categoryId: String,
    subCategoryId: String,
    questionType: String,
    onQuestionAdded: () -> Unit
) {
    val viewModel: AddQuestionViewModel = viewModel(factory = AddQuestionViewModelFactory(categoryId, subCategoryId))
    val uiState by viewModel.uiState.collectAsState()

    var questionText by remember { mutableStateOf("") }
    var options by remember { mutableStateOf(listOf("", "", "", "")) }
    var correctAnswer by remember { mutableStateOf("") }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onQuestionAdded()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val title = if (questionType == "mcq") "Add New MCQ" else "Add New SAQ"
        Text(text = title, style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = questionText,
            onValueChange = { questionText = it },
            label = { Text("Question Text") },
            modifier = Modifier.fillMaxWidth()
        )

        if (questionType == "mcq") {
            options.forEachIndexed { index, option ->
                OutlinedTextField(
                    value = option,
                    onValueChange = { newText ->
                        options = options.toMutableList().also { it[index] = newText }
                    },
                    label = { Text("Option ${index + 1}") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        OutlinedTextField(
            value = correctAnswer,
            onValueChange = { correctAnswer = it },
            label = { Text("Correct Answer") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                if (questionType == "mcq") {
                    viewModel.addMcqQuestion(questionText, options, correctAnswer)
                } else {
                    viewModel.addSaqQuestion(questionText, correctAnswer)
                }
            }) {
                Text("Save")
            }
        }

        uiState.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}