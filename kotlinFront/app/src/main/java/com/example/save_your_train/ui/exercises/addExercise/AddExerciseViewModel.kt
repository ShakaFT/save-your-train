package com.example.save_your_train.ui.exercises.addExercise

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.ExerciseDao
import com.example.save_your_train.data.addRemoteExercise
import kotlinx.coroutines.*
import java.io.IOException


class AddExerciseViewModel: ViewModel() {

    val addButtonClickable = MutableLiveData<Boolean>(false)
    val addButtonAlpha = MutableLiveData<Float>(alphaDisable)
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    // Public functions

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun addExercise(context: Context, exercise: Exercise): Boolean {
        val worked = GlobalScope.async {
            try {
                addRemoteExercise(exercise)
                insertExerciseDb(context, exercise)
                true
            } catch (e: java.nio.channels.UnresolvedAddressException) {
                displayError(true, "Une erreur est surevenue, veuillez réessayer plus tard...")
                false
            }
        }
        return worked.await()
    }

    fun onChangeTextName(context: Context, name: String) {
        if (name.isEmpty()) {
            disableAddButton(true)
            displayError(false)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val nameExists: Boolean = isInDatabase(context, name)
            disableAddButton(nameExists)
            displayError(nameExists, "Ce nom d'exercise est déjà utilisé")
        }
    }

    // Private functions

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    private fun disableAddButton(disabled: Boolean) {
        addButtonClickable.postValue(!disabled)
        addButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun insertExerciseDb(context: Context, exercise: Exercise) {
        val db = AppDatabase.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        exerciseDao.insertAll(exercise)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun isInDatabase(context: Context, name: String): Boolean {
        val exercise: Deferred<Exercise?> = GlobalScope.async {
            val db: AppDatabase = AppDatabase.getDatabase(context)
            val exerciseDao: ExerciseDao = db.exerciseDao()
            exerciseDao.findByName(name)
        }
        return exercise.await() != null
    }
}