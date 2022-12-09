package com.example.save_your_train.ui.exercises.launchedExercise

import android.os.Bundle
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
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}