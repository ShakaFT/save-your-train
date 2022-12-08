package com.example.save_your_train.ui.exercises.activeExercises

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.save_your_train.databinding.ActiveExerciseBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ActiveExerciseViewModel : ViewModel() {

    private var cases: List<ExercisesCaseModel> = listOf()

    fun activeButton(binding: ActiveExerciseBinding): Boolean {

        val currentCase = ExercisesCaseModel(
            binding.executionField.text.toString().isNotEmpty(),
            binding.repetitionField.text.toString().isNotEmpty(),
            binding.restField.text.toString().isNotEmpty(),
            binding.weightField.text.toString().isNotEmpty()
        )

        val activated: Boolean = this.cases.contains(currentCase)
        binding.exerciseLaunchButton.alpha = if (activated) 1F else 0.3F
        return activated
    }

    fun loadJson(context: Context) {
        val jsonString: String = context.assets.open("exerciseCases.json").bufferedReader().use {
            it.readText()
        }
        val listExercisesCaseType = object : TypeToken<List<ExercisesCaseModel>>() {}.type

        this.cases = Gson().fromJson(jsonString, listExercisesCaseType)
    }
}