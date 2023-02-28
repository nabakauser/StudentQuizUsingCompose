package com.example.studentquizusingcompose

import com.example.studentquizusingcompose.manager.QuizAssetManager
import com.example.studentquizusingcompose.repository.QuizRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

object ConfigurationClass {
    fun modules() = repositoryModule + viewModelModule + assetManager
}

val repositoryModule = module {
    single { QuizRepository(get()) }
}

val viewModelModule = module {
    single { QuizViewModel(get())}
}

val assetManager = module {
    single { QuizAssetManager(androidContext())}
}