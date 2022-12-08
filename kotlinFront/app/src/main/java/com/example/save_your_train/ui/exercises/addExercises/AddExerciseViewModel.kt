package com.example.save_your_train.ui.exercises.addExercises

import android.provider.Settings.Global
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.ExerciseDao
import com.example.save_your_train.databinding.AddExerciseLayoutBinding
import com.example.save_your_train.disableButton
import kotlinx.coroutines.*

class AddExerciseViewModel: ViewModel() {
    fun activeButton(binding: AddExerciseLayoutBinding) {

        if(binding.exerciseNameField.text.toString() == "") {
            disableButton(binding.exerciseAddButton,true)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            disableButton(binding.exerciseAddButton,!isNotInDataBase(binding))
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun isNotInDataBase(binding: AddExerciseLayoutBinding): Boolean {
        val exercise: Deferred<Exercise?> = GlobalScope.async {
            val db: AppDatabase = AppDatabase.getDatabase(binding.root.context)
            val exerciseDao: ExerciseDao = db.exerciseDao()
            println(1)
            exerciseDao.findByName(binding.exerciseNameField.text.toString())
        }
        return exercise.await() == null
    }
}