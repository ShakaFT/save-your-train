package com.example.save_your_train.ui.exercises.launchedExercise

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.databinding.LaunchedExerciseLayoutBinding

class LaunchedExerciseActivity: AppCompatActivity() {
    private lateinit var binding: LaunchedExerciseLayoutBinding
    private lateinit var launchedExerciseViewModel: LaunchedExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = LaunchedExerciseLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Get ViewModel
        launchedExerciseViewModel = ViewModelProvider(this)[LaunchedExerciseViewModel::class.java]

        //Get Parameters
        supportActionBar?.title = intent.getStringExtra("name")
        binding.timerForExecution.text = intent.getStringExtra("execution")
        binding.numberOfRepetition.text = intent.getStringExtra("repetition")
        binding.timerForRest.text = intent.getStringExtra("rest")
        binding.numberOfWeight.text = intent.getStringExtra("weight")
        binding.numberOfSeries.text = intent.getStringExtra("series")

        //Set Display
        launchedExerciseViewModel.initDisplay(
            binding.timerForExecution.text.toString(),
            binding.numberOfRepetition.text.toString(),
            binding.timerForRest.text.toString(),
            binding.numberOfWeight.text.toString(),
            binding.numberOfSeries.text.toString(),
        )
        binding.repetitionLabel.text = if(this.binding.numberOfRepetition.text.toString().toInt() == 1) "répétition" else "répétitions"

        //Set Observe
        setObserve()

    }

    private fun setObserve() {

        launchedExerciseViewModel.nbSeries.observe(this) {
            onChangeSeriesLabel(it)
        }

        launchedExerciseViewModel.displayExecution.observe(this) {
            displayTextView(it, binding.executionColumn)
        }

        launchedExerciseViewModel.displayRepetition.observe(this) {
            displayTextView(it, binding.repetitionLine)
        }

        launchedExerciseViewModel.displayRest.observe(this) {
            displayTextView(it, binding.restColumn)
        }

        launchedExerciseViewModel.displayWeight.observe(this) {
            displayTextView(it, binding.weightLine)
        }
    }

    private fun displayTextView(displayed: Boolean, linearLayout: LinearLayout) {
        linearLayout.visibility = if(displayed) View.VISIBLE else View.GONE
    }

    private fun onChangeSeriesLabel(nbSeries: String) {
        binding.seriesLabel.text = if(nbSeries.toInt() == 1) "série restante" else "séries restantes"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}