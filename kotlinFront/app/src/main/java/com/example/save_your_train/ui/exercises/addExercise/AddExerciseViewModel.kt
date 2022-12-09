package com.example.save_your_train.ui.exercises.addExercise

import android.content.Context
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
            val isInDatabase = !isNotInDataBase(binding)
            disableButton(binding.exerciseAddButton, isInDatabase)
            //displayTextView(binding.addExerciseError, isInDatabase)
        }
    }

    fun insertExerciseDb(exercise: Exercise, context: Context) {
        val db = AppDatabase.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exerciseDao.insertAll(exercise)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun isNotInDataBase(binding: AddExerciseLayoutBinding): Boolean {
        val exercise: Deferred<Exercise?> = GlobalScope.async {
            val db: AppDatabase = AppDatabase.getDatabase(binding.root.context)
            val exerciseDao: ExerciseDao = db.exerciseDao()
            exerciseDao.findByName(binding.exerciseNameField.text.toString())
        }
        return exercise.await() == null
    }
}