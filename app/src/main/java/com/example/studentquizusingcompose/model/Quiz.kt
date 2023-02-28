package com.example.studentquizusingcompose.model

import com.google.gson.annotations.SerializedName

data class Quiz (
    val quizTitle: String?,
    @SerializedName("status_code")
    val statusCode: Int?,
    val status: Boolean?,
    val message: String?,
    val questions: List<Question>?,
)