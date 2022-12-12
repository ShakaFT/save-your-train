package com.example.save_your_train.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HistoryViewModel : ViewModel() {

    val previousButtonClickable = MutableLiveData<Boolean>(false)
    val previousButtonAlpha = MutableLiveData<Float>(alphaDisable)

    val nextButtonClickable = MutableLiveData<Boolean>(false)
    val nextButtonAlpha = MutableLiveData<Float>(alphaDisable)

    private var page: Int = 1
    var histories: MutableList<History> = mutableListOf()
    val historiesList = MutableLiveData<MutableList<History>>()

    // Public functions

    fun previousPage() {
        this.page--
        this.disablePageButtons()
        this.updateDisplayedHistories()
    }

    fun nextPage() {
        this.page++
        this.disablePageButtons()
        this.updateDisplayedHistories()
    }

    fun refreshRecycler() {
        // Get all exercises in db
        CoroutineScope(Dispatchers.IO).launch {
            val historyDao: HistoryDao = AppDatabase.data!!.historyDao()
            histories = historyDao.getAll()

            // Pagination
            page = 1
            updateDisplayedHistories()
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
        return this.page * 10 >= this.histories.size
    }

    private fun updateDisplayedHistories() {
        // Calculate 10 histories that must displayed
        val start: Int = (this.page - 1) * 10
        val end: Int =  if (!this.isLastPage()) (this.page * 10 - 1) else (this.histories.size-1)

        historiesList.postValue(histories.slice(start..end).toMutableList())
    }
}