package com.example.save_your_train.ui.exercises.addExercise

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


class AddExerciseViewModel: ViewModel() {

    val isFinished = MutableLiveData<Boolean>(false)

    val addButtonClickable = MutableLiveData<Boolean>(false)
    val addButtonAlpha = MutableLiveData<Float>(alphaDisable)
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    // Public functions

    fun onClickAddButton(exercise: Exercise, accountDataStore: AccountDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            if (addExercise(exercise, accountDataStore)) {
                isFinished.postValue(true)
            }
        }
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
            displayError(nameExists, "Ce nom d'exercice est déjà utilisé")
        }
    }

    // Private functions

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun addExercise(exercise: Exercise, accountDataStore: AccountDataStore): Boolean {
        disableAddButton(true) // Disable button during process
        val worked = GlobalScope.async {
            try {
                addRemoteExercise(exercise, accountDataStore.getAccount.first()!!.email)
                insertExerciseDb(exercise)
                true
            } catch (e: Exception) {
                displayError(true, "Une erreur est survenue, veuillez réessayer plus tard...")
                false
            }
        }
        val result: Boolean = worked.await()
        disableAddButton(false)
        return result
    }

    private fun disableAddButton(disabled: Boolean) {
        addButtonClickable.postValue(!disabled)
        addButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    private fun insertExerciseDb(exercise: Exercise) {
        val exerciseDao = AppDatabase.data!!.exerciseDao()
        exerciseDao.insert(exercise)
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