package com.example.studentquizusingcompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun QuizRouter(
    quizViewModel: QuizViewModel
) {
    val uiState by quizViewModel.quizUiState.collectAsState()
    SurpriseQuizScreen(
        totalQuestions = uiState.questions.size,
        quizTitle = uiState.quizTitle,
        questionList = uiState.questions,
        onOptionSelected = { questionId, optionId ->
            quizViewModel.onOptionSelected(questionId, optionId)
        },
        countDownTimer = uiState.timeLeft,
        isBottomSheetExpanded = uiState.isBottomSheetExpanded,
        isQuizFinished = uiState.isQuizFinished
    )
}