package com.example.save_your_train.ui.exercises

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.ExerciseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ExercisesViewModel : ViewModel() {

    val previousButtonClickable = MutableLiveData<Boolean>(false)
    val previousButtonAlpha = MutableLiveData<Float>(alphaDisable)

    val nextButtonClickable = MutableLiveData<Boolean>(false)
    val nextButtonAlpha = MutableLiveData<Float>(alphaDisable)

    private var page: Int = 1
    var exercises: MutableList<Exercise> = mutableListOf()
    val exercisesList = MutableLiveData<MutableList<Exercise>>()

    // Public functions

    fun previousPage() {
        this.page--
        this.disablePageButtons()
        this.updateDisplayedExercises()
    }

    fun nextPage() {
        this.page++
        this.disablePageButtons()
        this.updateDisplayedExercises()
    }

    fun refreshRecycler() {
        // Get all exercises in db
        CoroutineScope(Dispatchers.IO).launch {
            val exerciseDao: ExerciseDao = AppDatabase.data!!.exerciseDao()
            exercises = exerciseDao.getAll()

            // Pagination
            page = 1
            updateDisplayedExercises()
            disablePageButtons()
        }
    }

    // Private functions

    private fun disablePageButtons() {
        previousButtonClickable.postValue(!this.isFirstPage())
        previousButtonAlpha.postValue(if (this.isFirstPage()) alphaDisable else alphaAble)

        nextButtonClickable.postValue(!this.isLastPage())
        nextButtonAlpha.postValue(if (this.isLastPage()) alphaDisable else alphaAble)
    }

    private fun isFirstPage(): Boolean {
        return this.page == 1
    }

    private fun isLastPage(): Boolean {
        return this.page * 10 >= this.exercises.size
    }

    private fun updateDisplayedExercises() {
        // Calculate 10 exercises that must displayed
        val start: Int = (this.page - 1) * 10
        val end: Int =  if (!this.isLastPage()) (this.page * 10 - 1) else (this.exercises.size-1)

        exercisesList.postValue(exercises.slice(start..end).toMutableList())
    }
}