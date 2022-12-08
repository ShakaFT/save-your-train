package com.example.save_your_train.ui.exercises.activeExercises

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.ExerciseCase
import com.example.save_your_train.databinding.ActiveExerciseBinding

class ActiveExerciseViewModel : ViewModel() {

    fun activeButton(context: Context, binding: ActiveExerciseBinding, exec: String, repet: String, rest: String, weight: String): Boolean {
        var exerciseCases = ExerciseCase(context)

        var currentCase = ExercisesCaseModel(
            exec.isNotEmpty(),
            repet.isNotEmpty(),
            rest.isNotEmpty(),
            weight.isNotEmpty())

        var activated: Boolean = exerciseCases.cases.contains(currentCase)
        binding.exerciseLaunchButton.alpha = if (activated) 1F else 0.3F
        return activated
    }
}