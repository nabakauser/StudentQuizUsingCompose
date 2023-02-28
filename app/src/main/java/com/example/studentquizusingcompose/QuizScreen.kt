package com.example.studentquizusingcompose

import android.annotation.SuppressLint
import android.os.CountDownTimer
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TitlePreview() {
    SurpriseQuizScreen(
        totalQuestions = 0,
        quizTitle = "",
        questionList = arrayListOf(),
        onOptionSelected = { _, _ ->
        },
        countDownTimer = "",
        isBottomSheetExpanded = false,
        isQuizFinished = false
    )
}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun SurpriseQuizScreen(
    totalQuestions: Int,
    quizTitle: String,
    questionList: List<QuestionsUi>,
    onOptionSelected: (String, String) -> Unit,
    countDownTimer: String,
    isBottomSheetExpanded: Boolean,
    isQuizFinished: Boolean
) {
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )

    Log.d("isQuizFinished",isQuizFinished.toString())

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = bottomSheetScaffoldState,
        sheetShape = RoundedCornerShape(topEnd = 30.dp, topStart = 30.dp),
        sheetContent = { SubmitQuizBottomSheet() },
        topBar = { QuizTitleBar(countDownTimer, totalQuestions, quizTitle) },
        sheetPeekHeight = 0.dp,
    ) {
        scope.launch {
            if (isQuizFinished) {
                bottomSheetScaffoldState.bottomSheetState.expand()
            } else {
                bottomSheetScaffoldState.bottomSheetState.collapse()
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.grey)),
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f)
            ) {
                itemsIndexed(questionList) { index, question ->
                    val questionNumber = index + 1
                    val formattedIndex = if (questionNumber < 10) {
                        String.format("0%d", questionNumber)
                    } else {
                        questionNumber.toString()
                    }
                    QuizCard(
                        questionId = "$formattedIndex.",
                        questionText = question.questionText,
                        optionList = question.options ?: listOf(),
                        onOptionSelected = { optionId ->
                            onOptionSelected(question.questionId ?: "", optionId)
                        }
                    )
                }
            }
            SubmitButton(
                scope = scope,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                isBottomSheetExpanded = isBottomSheetExpanded
            )
        }
    }
}


@Composable
fun QuizTitleBar(
    countDownTimer: String,
    totalQuestions: Int,
    quizTitle: String
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.appbarbackground),
            contentDescription = "title bar image",
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(colorResource(id = R.color.digi_blue).copy(.7f))
                .padding(all = 10.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = Icons.Default.ArrowBack,
                    //painterResource(id = R.drawable.ic_back_button),
                    contentDescription = "Back button",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = "Back",
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 15.sp
                    ),
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.padding(15.dp))
                Text(
                    text = quizTitle,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.padding(2.dp))
            Row(
                modifier = Modifier.padding(start = 30.dp)
            ) {
                Text(
                    text = "$totalQuestions Questions",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 15.sp
                    )

                )
                Spacer(modifier = Modifier.padding(5.dp))
                Text(
                    text = ". Quiz Timer: $countDownTimer",
                    color = Color.White,
                    style = TextStyle(
                        fontFamily = FontFamily.SansSerif,
                        fontSize = 15.sp
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizCardPreview() {
    QuizCard(
        questionId = "",
        questionText = "",
        optionList = arrayListOf(),
        onOptionSelected = {},
    )
}

@Composable
fun QuizCard(
    questionId: String?,
    questionText: String?,
    optionList: List<OptionUi>,
    onOptionSelected: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 10.dp),
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(all = 10.dp),
                    text = questionId ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = Modifier.padding(all = 10.dp),
                    text = questionText ?: "",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
            Divider(
                color = Color.LightGray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 2.dp),
                thickness = 2.dp,
            )

            optionList.forEach {
                QuizOption(
                    optionId = it.optionId ?: "",
                    optionText = it.optionText,
                    isSelected = it.isSelected,
                    onOptionSelected = onOptionSelected
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun OptionPreview() {
    QuizOption(optionId = "", optionText = "", isSelected = false, onOptionSelected = {})
}

@Composable
fun QuizOption(
    optionId: String,
    optionText: String?,
    isSelected: Boolean,
    onOptionSelected: (String) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = { onOptionSelected(optionId) }
            )
    ) {
        RadioButton(
            modifier = Modifier.align(alignment = Alignment.CenterVertically),
            selected = isSelected,
            onClick = { onOptionSelected(optionId) }
        )
        Text(
            text = optionText ?: "",
            modifier = Modifier.align(alignment = Alignment.CenterVertically)
        )
    }
}

@Composable
fun SubmitQuizBottomSheet() {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Quiz",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Text(
            text = "Submitted Successfully",
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Spacer(modifier = Modifier.padding(1.dp))
        Text(
            text = "Your answers are viewable once the Quiz ends",
            color = Color.DarkGray,
            style = TextStyle(
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp
            )
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Divider(
            color = Color.LightGray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 2.dp),
            thickness = 2.dp,
        )
        Spacer(modifier = Modifier.padding(10.dp))
        Button(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp)
                .fillMaxWidth(),
            border = BorderStroke(width = 1.dp, color = colorResource(id = R.color.digi_blue)),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(Color.White),
            onClick = {}
        ) {
            Text(
                text = "Close",
                color = colorResource(id = R.color.digi_blue),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SubmitButton(
    scope: CoroutineScope,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    isBottomSheetExpanded: Boolean
) {
    //var isBottomSheetExpanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 10.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(colorResource(id = R.color.digi_blue)),
            onClick = {
                scope.launch {
                    if (isBottomSheetExpanded) {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    } else {
                        bottomSheetScaffoldState.bottomSheetState.collapse()
                    }
                }
            }) {
            Text(
                text = "Submit",
                color = Color.White,
                fontSize = 18.sp
            )
        }
    }
}
