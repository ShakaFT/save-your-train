package com.example.save_your_train.ui.exercises.launchedExercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LaunchedExerciseViewModel: ViewModel() {

    var nbSeries = MutableLiveData<String>()
    var displayExecution = MutableLiveData<Boolean>()
    var displayRepetition = MutableLiveData<Boolean>()
    var displayRest = MutableLiveData<Boolean>()
    var displayWeight = MutableLiveData<Boolean>()

    fun initDisplay(execution: String, repetition: String, rest: String, weight: String, series: String) {
        nbSeries.postValue(series)
        displayExecution.postValue(execution.isNotEmpty())
        displayRepetition.postValue(repetition.isNotEmpty())
        displayRest.postValue(rest.isNotEmpty())
        displayWeight.postValue(weight.isNotEmpty())
    }
}