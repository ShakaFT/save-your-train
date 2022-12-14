package com.example.save_your_train.ui.exercises.launchedExercise

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.History
import com.example.save_your_train.data.addRemoteHistory
import kotlinx.coroutines.*


class LaunchedExerciseViewModel: ViewModel() {

    // Parameters
    private var name = ""
    private var series = "" // nbSeries before decrementation
    private var execution = ""
    private var repetition = ""
    private var rest = ""
    private var weight = ""

    // Mutable Live Data
    val isFinished = MutableLiveData<Boolean>(false)
    var nbSeries = MutableLiveData<String>()
    var buttonName = MutableLiveData<String>()
    var displayExecution = MutableLiveData<Boolean>()
    var displayRepetition = MutableLiveData<Boolean>()
    var displayRest = MutableLiveData<Boolean>(false)
    var displayWeight = MutableLiveData<Boolean>()

    val nextSeriesButtonClickable = MutableLiveData<Boolean>()
    val nextSeriesButtonAlpha = MutableLiveData<Float>()

    var textError = MutableLiveData<String>()
    var visibilityError = MutableLiveData<Int>(View.GONE)


    enum class ButtonNameCase(val buttonName: String) {
        NEXT("Prochaine série"),
        STOP("Terminer l'exercice"),
        REST("Lancer le repos")
    }


    fun initDisplay(name: String, execution: String, repetition: String, rest: String, weight: String, series: String) {
        // Method used to pass parameters
        this.name = name
        this.execution = execution
        this.repetition = repetition
        this.rest = rest
        this.weight = weight
        this.series = series

        this.nbSeries.value = series
        this.displayExecution.value = execution.isNotEmpty()
        this.displayRepetition.value = repetition.isNotEmpty()
        this.displayWeight.value = weight.isNotEmpty()
    }

    fun onClickNextSeriesButton() {
        if (this.rest.isNotEmpty()) updateDisplay()
        setButtonName()
        nextSeries()
    }

    fun setButtonName() {
        if (this.rest.isNotEmpty() && !this.displayRest.value!!) {
            this.buttonName.postValue(ButtonNameCase.REST.buttonName)
            return
        }
        if (this.rest.isNotEmpty() && this.displayRest.value!!) {
            this.buttonName.postValue(if(this.nbSeries.value!! != "1") ButtonNameCase.NEXT.buttonName else ButtonNameCase.STOP.buttonName)
            return
        }
        this.buttonName.postValue(if(this.nbSeries.value!! != "1") ButtonNameCase.NEXT.buttonName else ButtonNameCase.STOP.buttonName)
    }

    // Private functions

    private fun disableNextSeriesButton(disabled: Boolean) {
        nextSeriesButtonClickable.postValue(!disabled)
        nextSeriesButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    private fun insertHistoryDb(history: History) {
        val historyDao = AppDatabase.data!!.historyDao()
        historyDao.insertAll(history)
    }

    private fun nextSeries() {

        if (this.nbSeries.value != "1" && this.displayRest.value == false) this.nbSeries.postValue((this.nbSeries.value!!.toInt()-1).toString())
        if (this.nbSeries.value == "1" && this.displayRest.value == false) this.stopExercise()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun saveHistory(history: History): Boolean {
        disableNextSeriesButton(true) // Disable button during process
        val worked = GlobalScope.async {
            try {
                addRemoteHistory(history)
                insertHistoryDb(history)
                true
            } catch (e: Exception) {
                displayError(true, "Une erreur est survenue, veuillez réessayer plus tard...")
                false
            }
        }
        val result: Boolean = worked.await()
        disableNextSeriesButton(false)
        return result
    }

    private fun stopExercise() {
        this.displayWeight.value = false
        this.displayExecution.value = false
        this.displayRest.value = false
        this.displayRepetition.value = false

        val history = History(
            System.currentTimeMillis().toDouble(),
            name,
            execution,
            repetition,
            rest,
            series,
            weight
        )

        CoroutineScope(Dispatchers.IO).launch {
            if (saveHistory(history)) {
                isFinished.postValue(true)
            }
        }
    }

    private fun updateDisplay() {
        this.displayRest.value = (!this.displayRest.value!!)
        this.displayExecution.value = (if(!this.displayRest.value!!) this.execution.isNotEmpty() else false)
        this.displayRepetition.value = (if(!this.displayRest.value!!) this.repetition.isNotEmpty() else false)
        this.displayWeight.value = (if(!this.displayRest.value!!) this.weight.isNotEmpty() else false)
    }
}