package com.noveletytech.examsphere.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionTypeScreen(
    onTypeSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Question Type",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { onTypeSelected("mcq") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Multiple Choice Questions (MCQ)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onTypeSelected("saq") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Short Answer Questions (SAQ)")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onTypeSelected("laq") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Long Answer Questions (LAQ)")
        }
    }
}
