package com.example.save_your_train.ui.exercises.activeExercise

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.save_your_train.databinding.ActiveExerciseLayoutBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ActiveExerciseViewModel : ViewModel() {

    private var cases: List<ExercisesCaseModel> = listOf()

    fun activeButton(binding: ActiveExerciseLayoutBinding) {

        val currentCase = ExercisesCaseModel(
            binding.executionField.text.toString().isNotEmpty(),
            binding.repetitionField.text.toString().isNotEmpty(),
            binding.restField.text.toString().isNotEmpty(),
            binding.weightField.text.toString().isNotEmpty()
        )
        //disableButton(binding.exerciseLaunchButton, !cases.contains(currentCase))
    }

    fun loadJson(context: Context) {
        val jsonString: String = context.assets.open("exerciseCases.json").bufferedReader().use {
            it.readText()
        }


        val listExercisesCaseType = object : TypeToken<List<ExercisesCaseModel>>() {}.type

        this.cases = Gson().fromJson(jsonString, listExercisesCaseType)
    }
}