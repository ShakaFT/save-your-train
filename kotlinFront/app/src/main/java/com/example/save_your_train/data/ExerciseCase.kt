package com.example.save_your_train.data

import android.content.Context
import android.util.Log
import com.example.save_your_train.ui.exercises.activeExercises.ExercisesCaseModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class ExerciseCase(var context: Context) {
    var cases: List<ExercisesCaseModel> = listOf()

    init {
        loadJson()
    }

    private fun loadJson() {
        println("Je suis dans le loadjson")
        val jsonString: String
        try {
            jsonString = context.assets.open("exerciseCases.json").bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            println("Je return rien")
            return
        }

        Log.i("data", jsonString)

        val gson = Gson()
        val listExercisesCaseType = object : TypeToken<List<ExercisesCaseModel>>() {}.type

        this.cases = gson.fromJson(jsonString, listExercisesCaseType)
        cases.forEachIndexed { idx, case -> println(case) }
    }
}