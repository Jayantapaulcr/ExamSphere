package com.noveletytech.examsphere.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.data.Question

@Composable
fun QuizScreen(
    categoryId: String,
    subCategoryId: String,
    onSeeResult: () -> Unit,
    viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(categoryId, subCategoryId))
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (currentQuestion != null) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    IconButton(onClick = { /* TODO: Handle back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    Column {
                        Text(text = categoryId)
                        Text(text = "${uiState.questions.size} Questions")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { /* TODO: Handle quit */ }) {
                        Text(text = "Quit")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Question: ${uiState.currentQuestionIndex + 1}/${uiState.questions.size}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = currentQuestion.text)
                Spacer(modifier = Modifier.height(16.dp))

                when (currentQuestion) {
                    is Question.MCQ -> {
                        LazyColumn {
                            items(currentQuestion.options) { option ->
                                Button(
                                    onClick = { viewModel.onAnswerSelected(uiState.currentQuestionIndex, option) },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(text = option)
                                }
                            }
                        }
                    }
                    is Question.SAQ -> {
                        var answer by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = answer,
                            onValueChange = { answer = it },
                            label = { Text("Your Answer") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    is Question.LAQ -> {
                        var answer by remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = answer,
                            onValueChange = { answer = it },
                            label = { Text("Your Answer") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { viewModel.onPreviousClicked() }) {
                        Text(text = "Previous")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (uiState.currentQuestionIndex == uiState.questions.size - 1) {
                        Button(onClick = {
                            viewModel.calculateScore()
                            onSeeResult()
                        }) {
                            Text(text = "See Result")
                        }
                    } else {
                        Button(onClick = { viewModel.onNextClicked() }) {
                            Text(text = "Next")
                        }
                    }
                }
            } else {
                Text("No questions available.")
            }
        }
    }
}