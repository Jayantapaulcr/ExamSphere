package com.noveletytech.examsphere.branches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.branches.composables.BranchListItem

@Composable
fun BranchesScreen(
    categoryId: String,
    onBranchClicked: (String, String) -> Unit,
    viewModel: BranchesViewModel = viewModel(factory = BranchesViewModelFactory(categoryId))
) {
    val branches by viewModel.branches.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)) // Background color to make cards pop
            .padding(16.dp)
    ) {
        Text(
            text = "Branches",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
            color = MaterialTheme.colorScheme.primary
        )
        LazyColumn {
            items(branches, key = { it.key }) { branch ->
                BranchListItem(
                    name = branch.name,
                    imageUrl = branch.imageUrl,
                    modifier = Modifier.clickable { onBranchClicked(categoryId, branch.key) }
                )
            }
        }
    }
}
