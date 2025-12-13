package com.noveletytech.examsphere.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizScreen(
    categoryId: String, 
    onSeeResult: () -> Unit,
    viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(categoryId))
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        CircularProgressIndicator()
    } else {
        val currentQuestion = uiState.questions[uiState.currentQuestionIndex]
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
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
            Text(text = currentQuestion.question)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                itemsIndexed(currentQuestion.options) { index, option ->
                    Button(
                        onClick = { viewModel.onAnswerSelected(uiState.currentQuestionIndex, option) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = option)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = {
                viewModel.calculateScore()
                onSeeResult()
            }) {
                Text(text = "See Result")
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
                Button(onClick = { viewModel.onNextClicked() }) {
                    Text(text = "Next")
                }
            }
        }
    }
}