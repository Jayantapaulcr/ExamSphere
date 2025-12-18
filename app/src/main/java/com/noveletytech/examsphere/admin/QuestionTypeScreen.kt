package com.noveletytech.examsphere.admin

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
    onMcqClicked: () -> Unit,
    onSaqClicked: () -> Unit,
    onLaqClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Select Question Type", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onMcqClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Manage MCQs")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSaqClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Manage SAQs")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onLaqClicked, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Long Answer Questions")
        }
    }
}