package com.example.studentquizusingcompose.repository

import com.example.studentquizusingcompose.manager.QuizAssetManager
import com.example.studentquizusingcompose.mapper.QuizMapper
import com.example.studentquizusingcompose.model.Question
import com.example.studentquizusingcompose.model.Quiz
import com.example.studentquizusingcompose.utils.Resource

class QuizRepository( private val assetManager: QuizAssetManager){

    fun getQuiz(): Resource<List<Question>> {
        return QuizMapper.mapQuiz(assetManager.getGson())
    }

    fun getQuizTitle(): String {
        return assetManager.getGson().quizTitle ?: ""
    }
}