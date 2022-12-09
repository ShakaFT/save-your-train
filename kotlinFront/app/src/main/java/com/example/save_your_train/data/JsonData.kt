package com.example.save_your_train.data

import android.content.Context
import com.example.save_your_train.ui.exercises.activeExercise.ExercisesCaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object JsonData {
    var exerciseCases: List<ExercisesCaseModel> = listOf()

    // Public functions

    fun loadJsonData(context: Context) {
        this.exerciseCases = getExerciseCases(context) as List<ExercisesCaseModel>
    }

    // Private functions

    private fun getExerciseCases(context: Context): List<ExercisesCaseModel> {
        val listExercisesCaseType = object : TypeToken<List<ExercisesCaseModel>>() {}.type
        return Gson().fromJson(getJsonString(context, "exerciseCases.json"), listExercisesCaseType)
    }

    // Private utils functions

    private fun getJsonString(context: Context, path: String): String {
        return context.assets.open(path).bufferedReader().use {
            it.readText()
        }
    }
}

