package com.example.save_your_train.ui.exercises.addExercise

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.ExerciseDao
import com.example.save_your_train.data.addRemoteExercise
import kotlinx.coroutines.*


class AddExerciseViewModel: ViewModel() {

    val isFinished = MutableLiveData<Boolean>(false)

    val addButtonClickable = MutableLiveData<Boolean>(false)
    val addButtonAlpha = MutableLiveData<Float>(alphaDisable)
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    // Public functions

    fun onClickAddButton(exercise: Exercise) {
        CoroutineScope(Dispatchers.IO).launch {
            if (addExercise(exercise)) {
                isFinished.postValue(true)
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun addExercise(exercise: Exercise): Boolean {
        val worked = GlobalScope.async {
            try {
                addRemoteExercise(exercise)
                insertExerciseDb(exercise)
                true
            } catch (e: java.nio.channels.UnresolvedAddressException) {
                displayError(true, "Une erreur est surevenue, veuillez réessayer plus tard...")
                false
            }
        }
        return worked.await()
    }

    fun onChangeTextName(name: String) {
        viewModelScope.launch {

        }

        if (name.isEmpty()) {
            disableAddButton(true)
            displayError(false)
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val nameExists: Boolean = isInDatabase(name)
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

    private fun insertExerciseDb(exercise: Exercise) {
        val exerciseDao = AppDatabase.data!!.exerciseDao()
        exerciseDao.insertAll(exercise)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun isInDatabase(name: String): Boolean {
        val exercise: Deferred<Exercise?> = GlobalScope.async {
            val exerciseDao: ExerciseDao = AppDatabase.data!!.exerciseDao()
            exerciseDao.findByName(name)
        }
        return exercise.await() != null
    }
}