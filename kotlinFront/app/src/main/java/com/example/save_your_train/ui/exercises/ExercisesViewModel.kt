package com.example.save_your_train.ui.exercises

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExercisesViewModel : ViewModel() {

    private var page: Int = 1

    var exercises: MutableList<Exercise> = mutableListOf(
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test3", "ok3")
    )
    var displayExercises: MutableList<Exercise> = mutableListOf()

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }

    fun nextPage(adapter: ExerciseListAdapter) {
        this.page++
        this._setDisplayExercises()
        adapter.fillExercises((this.displayExercises))
    }

    fun previousPage(adapter: ExerciseListAdapter) {
        this.page--
        this._setDisplayExercises()
        adapter.fillExercises((this.displayExercises))
    }

    private fun _setDisplayExercises() {
        val start: Int = (this.page - 1) * 10
        val end: Int = this.page * 10
        this.displayExercises = exercises.slice(start..end) as MutableList<Exercise>
    }
}