package com.noveletytech.examsphere.subcategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.data.SubCategory

@Composable
fun SubCategoryScreen(
    categoryId: String,
    onSubCategoryClicked: (String, String) -> Unit,
    viewModel: SubCategoryViewModel = viewModel(factory = SubCategoryViewModelFactory(categoryId))
) {
    val subCategories by viewModel.subCategories.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (subCategories.isEmpty()) {
            CircularProgressIndicator()
        } else {
            LazyColumn {
                items(subCategories, key = { it.key }) { subCategory ->
                    SubCategoryListItem(
                        subCategory = subCategory,
                        modifier = Modifier.clickable { onSubCategoryClicked(categoryId, subCategory.key) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SubCategoryListItem(subCategory: SubCategory, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = subCategory.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.padding(top = 8.dp))
                Text(text = "MCQs: ${subCategory.mcqCount}")
                Text(text = "SAQs: ${subCategory.saqCount}")
            }
        }
    }
}