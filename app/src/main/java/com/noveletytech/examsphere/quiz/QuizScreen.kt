package com.noveletytech.examsphere.quiz

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.noveletytech.examsphere.data.Question

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    categoryId: String,
    branchId: String,
    subjectId: String,
    questionType: String,
    onFinishQuiz: () -> Unit,
    viewModel: QuizViewModel = viewModel(factory = QuizViewModelFactory(categoryId, branchId, subjectId, questionType))
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    } else if (uiState.questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions found.", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        // Group questions by chapter (using a Pair of index and name as key)
        val groupedQuestions = remember(uiState.questions) {
            uiState.questions.groupBy { it.chapterIndex to it.chapterName }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = when (questionType.lowercase()) {
                                    "mcq" -> "MCQ Questions"
                                    "saq" -> "SAQ Questions"
                                    "laq" -> "LAQ Questions"
                                    else -> "Practice Session"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.questions.size} Questions Available",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { onFinishQuiz() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        TextButton(onClick = { onFinishQuiz() }) {
                            Text("Quit", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(padding)
                    .padding(horizontal = 16.dp)
            ) {
                item { Spacer(modifier = Modifier.height(16.dp)) }

                groupedQuestions.forEach { (chapterKey, questions) ->
                    val (chapterIndex, chapterName) = chapterKey
                    
                    item {
                        // Chapter Header - only shown once per group of questions
                        Text(
                            text = if (chapterName.isEmpty()) "General Concepts" else "Chapter $chapterIndex: $chapterName",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp, top = 8.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    item {
                        // All questions for this chapter are in this single card
                        ChapterCard(
                            questions = questions,
                            uiState = uiState,
                            onAnswerSelected = { idx, ans -> viewModel.onAnswerSelected(idx, ans) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                item {
                    Button(
                        onClick = { onFinishQuiz() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Complete Study Session", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun ChapterCard(
    questions: List<Question>,
    uiState: QuizUiState,
    onAnswerSelected: (Int, String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            questions.forEachIndexed { index, question ->
                val overallIndex = uiState.questions.indexOf(question)
                QuestionItem(
                    question = question,
                    selectedAnswer = uiState.selectedAnswers[overallIndex],
                    onAnswerSelected = { ans -> onAnswerSelected(overallIndex, ans) }
                )
                if (index < questions.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 24.dp),
                        color = MaterialTheme.colorScheme.outlineVariant,
                        thickness = 1.dp
                    )
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    question: Question,
    selectedAnswer: String?,
    onAnswerSelected: (String) -> Unit
) {
    val hasSelectedSomething = selectedAnswer != null

    Column {
        Text(
            text = "Q${question.index}",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = question.text,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(20.dp))

        when (question) {
            is Question.MCQ -> {
                question.options.forEach { option ->
                    val isSelected = selectedAnswer == option
                    val isCorrectOption = option == question.correctAnswer
                    
                    val showAsCorrect = hasSelectedSomething && isCorrectOption
                    val showAsError = isSelected && !isCorrectOption
                    
                    val colorTheme = when {
                        showAsCorrect -> Color(0xFF4CAF50) // Professional Green for correct
                        showAsError -> MaterialTheme.colorScheme.error // Red for wrong
                        else -> MaterialTheme.colorScheme.outline
                    }

                    val containerColor = when {
                        showAsCorrect -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        showAsError -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                        else -> Color.Transparent
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .border(
                                width = if (isSelected || showAsCorrect) 2.dp else 1.dp,
                                color = colorTheme,
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable(enabled = !hasSelectedSomething) { onAnswerSelected(option) },
                        color = containerColor
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isSelected || showAsCorrect,
                                onClick = null,
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = colorTheme,
                                    unselectedColor = MaterialTheme.colorScheme.outline
                                )
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (showAsCorrect) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
            is Question.SAQ -> {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SOLUTION", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(question.correctAnswer, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            is Question.LAQ -> {
                // Analysis section
            }
        }
    }
}
