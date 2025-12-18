package com.noveletytech.examsphere.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.data.SubCategory

@Composable
fun CategoryDetailScreen(
    categoryId: String,
    onAddSubCategory: () -> Unit,
    onSubCategoryClicked: (String) -> Unit,
    viewModel: CategoryDetailViewModel = viewModel(factory = CategoryDetailViewModelFactory(categoryId))
) {
    val subCategories by viewModel.subCategories.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddSubCategory) {
                Icon(Icons.Default.Add, contentDescription = "Add Sub-Category")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn {
                items(subCategories, key = { it.key }) { subCategory ->
                    SubCategoryRow(subCategory = subCategory, modifier = Modifier.clickable { onSubCategoryClicked(subCategory.key) })
                }
            }
        }
    }
}

@Composable
private fun SubCategoryRow(subCategory: SubCategory, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = subCategory.name, modifier = Modifier.padding(16.dp))
    }
}