package com.noveletytech.examsphere.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizScreen(onSeeResult: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            IconButton(onClick = { /* TODO: Handle back */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Column {
                Text(text = "HTML")
                Text(text = "30 Question")
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { /* TODO: Handle quit */ }) {
                Text(text = "Quit")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Question: 3/30")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Who is making the Web standards?")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "The World Wide Web Consortium")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Microsoft")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Mozilla")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO */ }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Google")
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onSeeResult) {
            Text(text = "See Result")
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { /* TODO: Previous */ }) {
                Text(text = "Previous")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /* TODO: Next */ }) {
                Text(text = "Next")
            }
        }
    }
}