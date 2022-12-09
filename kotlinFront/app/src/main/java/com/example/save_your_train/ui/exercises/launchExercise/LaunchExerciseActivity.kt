package com.example.save_your_train.ui.exercises.launchExercise

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.save_your_train.databinding.LaunchExerciseLayoutBinding

class LaunchExerciseActivity: AppCompatActivity() {
    private lateinit var binding: LaunchExerciseLayoutBinding
    private lateinit var launchExerciseViewModel: LaunchExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = LaunchExerciseLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}