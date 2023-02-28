package com.example.studentquizusingcompose.model

import com.google.gson.annotations.SerializedName

data class Option (
    @SerializedName("_id")
    val optionId: String?,
    val text: String?,
    var answer: Boolean?,
    var isSelected: Boolean = false,
        )