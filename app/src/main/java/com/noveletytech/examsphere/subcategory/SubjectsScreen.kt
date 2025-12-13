package com.noveletytech.examsphere.subcategory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.noveletytech.examsphere.subcategory.composables.SubjectListItem

@Composable
fun SubjectsScreen(
    categoryId: String,
    branchId: String,
    onSubjectClicked: (String, String, String) -> Unit,
    viewModel: SubjectsViewModel = viewModel(factory = SubjectsViewModelFactory(categoryId, branchId))
) {
    val subjects by viewModel.subjects.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Subjects",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        if (subjects.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(subjects, key = { it.key }) { subject ->
                    SubjectListItem(
                        subject = subject,
                        modifier = Modifier.clickable { onSubjectClicked(categoryId, branchId, subject.key) }
                    )
                }
            }
        }
    }
}
