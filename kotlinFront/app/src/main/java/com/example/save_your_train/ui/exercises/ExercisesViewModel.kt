package com.example.save_your_train.ui.exercises

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.databinding.FragmentExercisesBinding

class ExercisesViewModel : ViewModel() {

    private var page: Int = 1

    var exercises: MutableList<Exercise> = mutableListOf(
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test3", "ok3"),
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test", "ok"),
        Exercise("test2", "ok2"),
        Exercise("test", "ok4"),
        Exercise("test2", "ok3")
    )

    var displayExercises: MutableList<Exercise> = mutableListOf()

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
    }

    fun nextPage(adapter: ExerciseListAdapter, binding: FragmentExercisesBinding) {
        this.page++
        this.setDisplayExercises()
        adapter.fillExercises((this.displayExercises))
        binding.nextExerciseButton.isClickable = this.page != this.getMaxPage()
    }

    fun previousPage(adapter: ExerciseListAdapter, binding: FragmentExercisesBinding) {
        this.page--
        this.setDisplayExercises()
        adapter.fillExercises((this.displayExercises))
        binding.previousExerciseButton.isClickable = this.page != 1
    }

    fun getMaxPage(): Int {
        return this.exercises.size/10
    }

    private fun setDisplayExercises() {
        val start: Int = (this.page - 1) * 10
        var end: Int =  (this.page * 10 - 1)
        if(this.exercises.size-(this.page * 10 - 1) < 10 && this.exercises.size-((this.page - 1) * 10) >= 10) {
            end = this.exercises.size-1
        }
        this.displayExercises = exercises.slice(start..end) as MutableList<Exercise>
    }
}