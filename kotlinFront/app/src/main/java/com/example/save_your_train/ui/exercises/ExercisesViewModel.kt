package com.example.save_your_train.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExercisesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is exercises Fragment"
    }

    private val _exercises = arrayListOf("test", "bonjour", "test2")
        val text: LiveData<String> = _text
        val exercises: MutableList<String> = _exercises
}