package com.example.save_your_train.ui.exercises.launchedExercise

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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

        //GetParameters
        supportActionBar?.title = intent.getStringExtra("name")
        binding.timerForExecution.text = intent.getStringExtra("execution")
        binding.numberOfRepetition.text = intent.getStringExtra("repetition")
        binding.timerForRest.text = intent.getStringExtra("rest")
        binding.numberOfWeight.text = intent.getStringExtra("weight")
        binding.numberOfSeries.text = intent.getStringExtra("series")

        //Set Display
        displayTextView(binding.timerForExecution, binding.executionLabel)
        displayTextView(binding.numberOfRepetition, binding.repetitionLabel)
        displayTextView(binding.timerForRest, binding.restLabel)
        displayTextView(binding.numberOfWeight, binding.weightLabel)
        displayTextView(binding.numberOfSeries, binding.seriesLabel)

    }

    private fun displayTextView(valueTextView: TextView, labelTextView: TextView) {
        if(valueTextView.text.toString().isEmpty()) {
            valueTextView.visibility = View.GONE
            labelTextView.visibility = View.GONE
            return
        }
        valueTextView.visibility = View.VISIBLE
        labelTextView.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}