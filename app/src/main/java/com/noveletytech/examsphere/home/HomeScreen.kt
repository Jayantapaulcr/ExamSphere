package com.noveletytech.examsphere.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.home.composables.CategoryItem
import com.noveletytech.examsphere.home.composables.RecentActivityItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, 
    onPlayClicked: (String) -> Unit,
    onSeeAllClicked: () -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.AccountCircle, contentDescription = "User Avatar")
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(text = "Player 1")
                Text(text = "ID-1800")
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Diamond, contentDescription = "Points")
                Text(text = "160")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(
                        text = "Test Your Knowledge with Quizzes",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { onPlayClicked(categories.firstOrNull()?.key ?: "") }) {
                        Text(text = "Play Now")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Filled.Star, contentDescription = "Star Icon", modifier = Modifier.padding(start = 16.dp))
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = "",
            onValueChange = {},
            label = { Text("Search") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Tune, contentDescription = "Filter")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Categories", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = onSeeAllClicked) {
                Text(text = "See All")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(categories, key = { it.key }) { category ->
                CategoryItem(
                    imageUrl = category.imageUrl, 
                    name = category.name,
                    modifier = Modifier.clickable { onPlayClicked(category.key) }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Recent Activity", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(recentActivities, key = { it.key }) { activity ->
                val progress = if (activity.total > 0) activity.score.toFloat() / activity.total.toFloat() else 0f
                RecentActivityItem(
                    name = activity.name, 
                    imageUrl = activity.imageUrl,
                    questions = activity.questions, 
                    score = "${activity.score}/${activity.total}",
                    progress = progress
                )
            }
        }
    }
}