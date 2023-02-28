package com.example.studentquizusingcompose.mapper

import com.example.studentquizusingcompose.model.Question
import com.example.studentquizusingcompose.model.Quiz
import com.example.studentquizusingcompose.utils.Resource

class QuizMapper {
    companion object {
        fun mapQuiz(quiz: Quiz): Resource<List<Question>> {
            return if(quiz.status == true && quiz.statusCode == 200) {
                Resource.success(quiz.questions)
            } else {
                Resource.error(quiz.message ?: "")
            }
        }
    }
}