package com.noveletytech.examsphere.categories.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun CategoryListItem(name: String, imageUrl: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(vertical = 4.dp)) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.padding(start = 16.dp))
            Text(text = name, style = MaterialTheme.typography.titleMedium)
        }
    }
}