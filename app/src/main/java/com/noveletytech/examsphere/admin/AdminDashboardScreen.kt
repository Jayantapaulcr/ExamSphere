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
import com.noveletytech.examsphere.data.AuthRepository

@Composable
fun AdminDashboardScreen(onSignOut: () -> Unit, onManageCategories: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Admin Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onManageCategories, modifier = Modifier.fillMaxWidth()) {
            Text("Manage Categories")
        }
        // TODO: Add button for Manage Questions

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = {
            AuthRepository().signOut()
            onSignOut()
        }) {
            Text("Sign Out")
        }
    }
}