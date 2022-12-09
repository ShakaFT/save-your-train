package com.example.save_your_train.ui.exercises

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.ExerciseDao
import com.example.save_your_train.databinding.FragmentExercisesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExercisesViewModel : ViewModel() {

    var page: Int = 1
    var exercises: MutableList<Exercise> = mutableListOf()

    fun previousPage(adapter: ExerciseListAdapter, binding: FragmentExercisesBinding) {
        this.page--
        this.setClickable(binding)
        this.updateDisplayedExercises(adapter)
    }

    fun nextPage(adapter: ExerciseListAdapter, binding: FragmentExercisesBinding) {
        this.page++
        this.setClickable(binding)
        updateDisplayedExercises(adapter)
    }

    fun setClickable(binding: FragmentExercisesBinding) {
        binding.previousExerciseButton.isClickable = this.page != 1
        binding.nextExerciseButton.isClickable = !this.isLastPage()
    }

    fun setData(context: Context, adapter: ExerciseListAdapter, binding: FragmentExercisesBinding) {
        // get all exercises in db
        CoroutineScope(Dispatchers.IO).launch {
            // val db: AppDatabase = AppDatabase.getDatabase(context)
            val exerciseDao: ExerciseDao = AppDatabase.data!!.exerciseDao()
            exercises = exerciseDao.getAll()

            // Pagination
            page = 1
            updateDisplayedExercises(adapter)
            setClickable(binding)
        }
    }

    private fun isLastPage(): Boolean {
        return this.page * 10 >= this.exercises.size
    }

    private fun updateDisplayedExercises(adapter: ExerciseListAdapter) {
        // Calculate 10 exercises that must displayed
        // and displayed theses exercises
        val start: Int = (this.page - 1) * 10
        val end: Int =  if (!this.isLastPage()) (this.page * 10 - 1) else (this.exercises.size-1)

        adapter.fillExercises(exercises.slice(start..end).toMutableList())
        adapter.notifyDataSetChanged()
    }
}