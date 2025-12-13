package com.noveletytech.examsphere.chapters.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChapterListItem(name: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = name, modifier = Modifier.padding(16.dp))
    }
}
