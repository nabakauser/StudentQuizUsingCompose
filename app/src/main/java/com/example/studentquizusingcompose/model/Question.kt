package com.example.studentquizusingcompose.model

import com.google.gson.annotations.SerializedName

data class Question (
    @SerializedName("_id")
    val id: String?,
    val text: String?,
    val questionType: String?,
    val options: List<Option>?,
)