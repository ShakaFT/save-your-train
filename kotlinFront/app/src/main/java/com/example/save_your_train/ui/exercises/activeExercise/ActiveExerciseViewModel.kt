package com.example.save_your_train.ui.exercises.activeExercise

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.JsonData
import com.example.save_your_train.data.removeRemoteExercise
import kotlinx.coroutines.*


class ActiveExerciseViewModel : ViewModel() {

    val isLaunched = MutableLiveData<Boolean>(false)
    val isFinished = MutableLiveData<Boolean>(false)

    val launchButtonClickable = MutableLiveData<Boolean>(false)
    val launchButtonAlpha = MutableLiveData<Float>(alphaDisable)
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

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

    fun onClickRemoveExercise(exercise: Exercise) {
        CoroutineScope(Dispatchers.IO).launch {
            if (removeExercise(exercise)) {
                isFinished.postValue(true)
            }
        }
    }

    // Private methods

    private fun disableLaunchButton(disabled: Boolean) {
        launchButtonClickable.postValue(!disabled)
        launchButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun removeExercise(exercise: Exercise): Boolean {
        val worked = GlobalScope.async {
            try {
                removeRemoteExercise(exercise)
                removeExerciseDb(exercise)
                true
            }
            catch (e: Exception) {
                displayError(true, "Une erreur est surevenue, veuillez réessayer plus tard...")
                false
            }
        }
        return worked.await()
    }

    private fun removeExerciseDb(exercise: Exercise) {
        val exerciseDao = AppDatabase.data!!.exerciseDao()
        exerciseDao.delete(exercise)
    }
}