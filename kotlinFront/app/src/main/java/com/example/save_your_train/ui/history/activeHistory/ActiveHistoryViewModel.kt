package com.example.save_your_train.ui.history.activeHistory

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.History
import com.example.save_your_train.data.removeRemoteHistory
import kotlinx.coroutines.*

class ActiveHistoryViewModel: ViewModel() {

    val isFinished = MutableLiveData<Boolean>(false)

    val deleteButtonClickable = MutableLiveData<Boolean>()
    val deleteButtonAlpha = MutableLiveData<Float>()

    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)
    // Public methods

    fun onClickRemoveHistory(history: History) {
        CoroutineScope(Dispatchers.IO).launch {
            if (removeHistory(history)) {
                isFinished.postValue(true)
            }
        }
    }

    // Private methods

    private fun disableDeleteButton(disabled: Boolean) {
        deleteButtonClickable.postValue(!disabled)
        deleteButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun removeHistory(history: History): Boolean {
        disableDeleteButton(true) // Disable button during process
        val worked = GlobalScope.async {
            try {
                removeRemoteHistory(history)
                removeHistoryDb(history)
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

    private fun removeHistoryDb(history: History) {
        val historyDao = AppDatabase.data!!.historyDao()
        historyDao.delete(history)
    }
}