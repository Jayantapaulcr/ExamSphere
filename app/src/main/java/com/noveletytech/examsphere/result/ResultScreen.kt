package com.noveletytech.examsphere.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
fun ResultScreen(onBackToHome: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Your Score", style = MaterialTheme.typography.headlineMedium)
        Text(text = "29/30", style = MaterialTheme.typography.displayLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Congratulation", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Great job, Rumi Aktar! You Did It")
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { /* TODO: Share */ }) {
            Text(text = "Share")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onBackToHome) {
            Text(text = "Back to Home")
        }
    }
}