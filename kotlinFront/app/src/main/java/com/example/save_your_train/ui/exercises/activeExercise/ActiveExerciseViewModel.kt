package com.example.save_your_train.ui.exercises.activeExercise

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first


class ActiveExerciseViewModel : ViewModel() {

    val isLaunched = MutableLiveData<Boolean>(false)
    val isFinished = MutableLiveData<Boolean>(false)

    val deleteButtonClickable = MutableLiveData<Boolean>()
    val deleteButtonAlpha = MutableLiveData<Float>()

    val launchButtonClickable = MutableLiveData<Boolean>(false)
    val launchButtonAlpha = MutableLiveData<Float>(alphaDisable)
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    val execution = MutableLiveData<String>()
    val rest = MutableLiveData<String>()
    val repetition = MutableLiveData<String>()
    val weight = MutableLiveData<String>()
    val series = MutableLiveData<String>()
    // Public methods

    fun onChangeText(exec: String, repet: String, rest: String, weight: String) {
        val currentCase = ExercisesCaseModel(
            exec.isNotEmpty(),
            repet.isNotEmpty(),
            rest.isNotEmpty(),
            weight.isNotEmpty()
        )
        disableLaunchButton(!JsonData.exerciseCases.contains(currentCase))
    }

    fun onClickLaunchButton() {
        isLaunched.postValue(true)
    }

    fun onClickRemoveExercise(exercise: Exercise, accountDataStore: AccountDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            if (removeExercise(exercise, accountDataStore)) {
                isFinished.postValue(true)
            }
        }
    }

    // Private methods

    private fun disableDeleteButton(disabled: Boolean) {
        deleteButtonClickable.postValue(!disabled)
        deleteButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun disableLaunchButton(disabled: Boolean) {
        launchButtonClickable.postValue(!disabled)
        launchButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun removeExercise(exercise: Exercise, accountDataStore: AccountDataStore): Boolean {
        disableDeleteButton(true) // Disable button during process
        val worked = GlobalScope.async {
            try {
                removeRemoteExercise(exercise, accountDataStore.getAccount.first()!!.email)
                removeExerciseDb(exercise)
                true
            }
            catch (e: Exception) {
                displayError(true, "Une erreur est survenue, veuillez réessayer plus tard...")
                false
            }
        }
        val result: Boolean = worked.await()
        disableDeleteButton(false)
        return result
    }

    private fun removeExerciseDb(exercise: Exercise) {
        val exerciseDao = AppDatabase.data!!.exerciseDao()
        exerciseDao.delete(exercise)
    }

    public fun fillExercise(exerciseName: String){
        // Get first history in db
        CoroutineScope(Dispatchers.IO).launch {
            val historyDao: HistoryDao = AppDatabase.data!!.historyDao()
            val histories = historyDao.findByName(exerciseName)
            if(histories.isNotEmpty()) {
                val history = histories.first()
                execution.postValue(history.execution)
                rest.postValue(history.rest)
                repetition.postValue(history.repetition)
                weight.postValue(history.weight)
                if (history.series != "1") series.postValue(history.series)
            }
        }
    }
}