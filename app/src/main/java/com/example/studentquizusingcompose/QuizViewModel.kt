package com.example.studentquizusingcompose

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentquizusingcompose.model.Question
import com.example.studentquizusingcompose.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class QuizViewModel(
    private val quizRepository: QuizRepository
) : ViewModel() {

    private val viewModelState = MutableStateFlow(QuizViewModelState())
    private var countDownTimer: CountDownTimer? = null
//    private var countDownTimer: Job? = null

    val quizUiState = viewModelState
        .map { it.quizToUiState() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, viewModelState.value.quizToUiState())

    private fun getQuiz() {
        val response = quizRepository.getQuiz()
        val quizTitle = quizRepository.getQuizTitle()
        if (response.data != null) {
            viewModelState.update {
                it.copy(
                    quizTitle = quizTitle,
                    questionList = mapQuizUi(response.data)
                )
            }
        }
    }

    init {
        getQuiz()
        startCountDownTimer()
    }

//    private fun startTimer() {
//        countDownTimer = viewModelScope.launch {
//            var countDownTime = 120
//            while(countDownTime >=0) {
//                val formattedTime = String.format("%02d:%02d", countDownTime / 60, countDownTime % 60)
//                viewModelState.update {
//                    it.copy(
//                        timeLeft = formattedTime
//                    )
//                }
//                delay(1000)
//                countDownTime -= 1
//            }
//        }
//    }


    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(120000, 1000) {
            override fun onTick(timeUntilFinished: Long) {
                val countDownTime = timeUntilFinished / 1000
                val formattedTime =
                    String.format("%02d:%02d", countDownTime / 60, countDownTime % 60)
                viewModelState.update {
                    it.copy(
                        timeLeft = formattedTime
                    )
                }
            }

            override fun onFinish() {
                viewModelState.update {
                    it.copy(
                        timeLeft = "Quiz Completed",
                        isBottomSheetExpanded = true,
                        isQuizFinished = true
                    )
                }
            }
        }
        (countDownTimer as CountDownTimer).start()

    }

    private fun mapQuizUi(questionList: List<Question>): List<QuestionsUi> {
        return questionList.map { question ->
            QuestionsUi(
                questionId = question.id,
                questionText = question.text,
                options = question.options?.map { option ->
                    OptionUi(
                        optionId = option.optionId,
                        optionText = option.text,
                        isSelected = option.isSelected
                    )
                }
            )
        }
    }

    fun onOptionSelected(questionId: String?, optionId: String?) {
        val questions = quizUiState.value.questions
        val question = questions.find {
            it.questionId == questionId
        }
        if (question != null) {
            question.options?.forEach { option ->
//                option.isSelected = option.optionId == optionId
                option.isSelected = false
                if (option.optionId == optionId) {
                    option.isSelected = true
                }
            }
        }

        viewModelState.update {
            it.copy(
                questionList = questions,
                lastUpdate = System.currentTimeMillis(),
            )
        }
    }
}

data class QuizViewModelState(
    val totalQuestions: Int = 0,
    val quizTitle: String = "",
    val questionList: List<QuestionsUi> = mutableListOf(),
    val lastUpdate: Long = System.currentTimeMillis(),
    val timeLeft: String = 6000.toString(),
    val isBottomSheetExpanded: Boolean = false,
    val isQuizFinished: Boolean = false

) {
    fun quizToUiState(): QuizViewModelUi {
        return QuizViewModelUi(
            totalQuestions = questionList.size,
            quizTitle = quizTitle,
            questions = questionList,
            lastUpdate = lastUpdate,
            timeLeft = timeLeft,
            isBottomSheetExpanded = isBottomSheetExpanded,
            isQuizFinished = isQuizFinished
        )
    }
}

data class QuizViewModelUi(
    val totalQuestions: Int,
    val quizTitle: String,
    val questions: List<QuestionsUi>,
    val lastUpdate: Long,
    val timeLeft: String,
    val isBottomSheetExpanded: Boolean,
    val isQuizFinished: Boolean,
)

data class QuestionsUi(
    val questionId: String?,
    val questionText: String?,
    val options: List<OptionUi>?,
)

data class OptionUi(
    val optionId: String?,
    val optionText: String?,
    var isSelected: Boolean,
)