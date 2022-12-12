package com.example.save_your_train.ui.exercises.launchedExercise

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.History
import io.ktor.util.date.*
import kotlinx.coroutines.*
import java.time.Instant

class LaunchedExerciseViewModel: ViewModel() {

    private var name = ""
    private var series = ""
    private var execution = ""
    private var repetition = ""
    private var rest = ""
    private var weight = ""

    val isFinished = MutableLiveData<Boolean>(false)
    var nbSeries = MutableLiveData<String>()
    var buttonName = MutableLiveData<String>()
    var displayExecution = MutableLiveData<Boolean>()
    var displayRepetition = MutableLiveData<Boolean>()
    var displayRest = MutableLiveData(false)
    var displayWeight = MutableLiveData<Boolean>()

    var hasRest = false

    enum class ButtonNameCase(val buttonName: String) {
        NEXT("Prochaine série"),
        STOP("Terminer l'exercice"),
        REST("Lancer le repos")
    }

    fun initDisplay(name: String, execution: String, repetition: String, rest: String, weight: String, series: String) {
        this.nbSeries.value = series
        this.name = name
        this.series = series
        this.execution = execution
        this.repetition = repetition
        this.rest = rest
        this.weight = weight
        this.displayExecution.value = execution.isNotEmpty()
        this.displayRepetition.value = repetition.isNotEmpty()
        this.displayWeight.value = weight.isNotEmpty()
        this.hasRest = rest.isNotEmpty()
    }

    fun nextSeries() {
        if(this.nbSeries.value != "1" && this.displayRest.value == false) this.nbSeries.postValue((this.nbSeries.value!!.toInt()-1).toString())
        if(this.nbSeries.value == "1" && this.displayRest.value == false) this.stopExercise()
    }

    fun setButtonName() {
        if(this.hasRest && !this.displayRest.value!!) {
            this.buttonName.postValue(ButtonNameCase.REST.buttonName)
            return
        }
        if(this.hasRest && this.displayRest.value!!) {
            this.buttonName.postValue(if(this.nbSeries.value!!.toInt() != 1) ButtonNameCase.NEXT.buttonName else ButtonNameCase.STOP.buttonName)
            return
        }
        this.buttonName.postValue(if(this.nbSeries.value!!.toInt() != 1) ButtonNameCase.NEXT.buttonName else ButtonNameCase.STOP.buttonName)
    }

    fun updateDisplay() {
        this.displayRest.value = (!this.displayRest.value!!)
        this.displayExecution.value = (if(!this.displayRest.value!!) this.execution.isNotEmpty() else false)
        this.displayRepetition.value = (if(!this.displayRest.value!!) this.repetition.isNotEmpty() else false)
        this.displayWeight.value = (if(!this.displayRest.value!!) this.weight.isNotEmpty() else false)
    }

    private fun stopExercise() {
        this.displayWeight.value = false
        this.displayExecution.value = false
        this.displayRest.value = false
        this.displayRepetition.value = false

        val history = History(System.currentTimeMillis().toDouble(), name, execution, repetition, rest, series, weight)

        CoroutineScope(Dispatchers.IO).launch {
            if (saveHistory(history)) {
                isFinished.postValue(true)
            }
        }
    }

    private fun displayError(display: Boolean, text: String = "") {
        //textError.postValue(text)
        //visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun saveHistory(history: History): Boolean {
        val worked = GlobalScope.async {
            try {
                //addRemoteHistory(history)
                insertHistoryDb(history)
                true
            } catch (e: Exception) {
                //displayError(true, "Une erreur est survenue, veuillez réessayer plus tard...")
                false
            }
        }
        return worked.await()
    }

    private fun insertHistoryDb(history: History) {
        val historyDao = AppDatabase.data!!.historyDao()
        historyDao.insertAll(history)
    }

}