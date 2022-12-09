package com.example.save_your_train.ui.exercises.activeExercise

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class ActiveExerciseViewModel : ViewModel() {

    val isLaunched = MutableLiveData<Boolean>(false)
    val launchButtonClickable = MutableLiveData<Boolean>(false)
    val launchButtonAlpha = MutableLiveData<Float>(alphaDisable)

    private var cases: List<ExercisesCaseModel> = listOf()


    // Public methods

    fun loadJson(context: Context) {
        val jsonString: String = context.assets.open("exerciseCases.json").bufferedReader().use {
            it.readText()
        }


        val listExercisesCaseType = object : TypeToken<List<ExercisesCaseModel>>() {}.type

        this.cases = Gson().fromJson(jsonString, listExercisesCaseType)
    }

    fun onChangeText(exec: String, repet: String, rest: String, weight: String) {
        val currentCase = ExercisesCaseModel(
            exec.isNotEmpty(),
            repet.isNotEmpty(),
            rest.isNotEmpty(),
            weight.isNotEmpty()
        )
        disableLaunchButton(!cases.contains(currentCase))
    }

    fun onClickLaunchButton() {
        isLaunched.postValue(true)
    }

    // Private methods

    private fun disableLaunchButton(disabled: Boolean) {
        launchButtonClickable.postValue(!disabled)
        launchButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }
}