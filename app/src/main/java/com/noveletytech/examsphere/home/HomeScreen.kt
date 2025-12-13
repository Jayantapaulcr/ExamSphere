package com.noveletytech.examsphere.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.noveletytech.examsphere.home.composables.CategoryItem
import com.noveletytech.examsphere.home.composables.RecentActivityItem
import com.noveletytech.examsphere.home.composables.UpdatesCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, 
    onPlayClicked: (String) -> Unit,
    onSeeAllClicked: () -> Unit,
    viewModel: HomeViewModel
) {
    val categories by viewModel.categories.collectAsState()
    val recentActivities by viewModel.recentActivities.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val updateImageUrls by viewModel.updateImageUrls.collectAsState()
    val appConfig by viewModel.appConfig.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            if (currentUser?.profilePictureUrl?.isNotEmpty() == true) {
                AsyncImage(
                    model = currentUser?.profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Filled.AccountCircle, 
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(45.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = currentUser?.firstName ?: "User",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Diamond, contentDescription = "Points", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "160")
            }
        }
        
        if (appConfig.showCarousel && updateImageUrls.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            UpdatesCard(
                imageUrls = updateImageUrls,
                animationSpeed = appConfig.animationSpeed // Pass the dynamic speed here
            )
        }

        if (appConfig.dailyUpdate.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TipsAndUpdates,
                        contentDescription = "Update",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = appConfig.dailyUpdate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search your favorites...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            trailingIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Tune, contentDescription = "Filter")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = CircleShape,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
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
                    modifier = Modifier.clickable { 
                        viewModel.addCategoryToRecents(category)
                        onPlayClicked(category.key) 
                    }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Recent Activity", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            if (recentActivities.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearRecentActivities() }) {
                    Text(text = "Clear All", color = MaterialTheme.colorScheme.error)
                }
            }
        }
        
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