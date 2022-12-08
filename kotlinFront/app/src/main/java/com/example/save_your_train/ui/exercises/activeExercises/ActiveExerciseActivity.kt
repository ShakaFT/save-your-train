package com.example.save_your_train.ui.exercises.activeExercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.databinding.ActiveExerciseBinding

class ActiveExerciseActivity: AppCompatActivity() {

    private lateinit var binding: ActiveExerciseBinding
    private lateinit var activeExerciseViewModel:  ActiveExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActiveExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activeExerciseViewModel = ViewModelProvider(this)[ActiveExerciseViewModel::class.java]

        binding.repetitionField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                binding.exerciseLaunchButton.isClickable = activeExerciseViewModel.activeButton(
                    binding.root.context,
                    binding,
                    binding.executionField.text.toString(),
                    binding.repetitionField.text.toString(),
                    binding.restField.text.toString(),
                    binding.weightField.text.toString())
            }
        })

        binding.executionField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                binding.exerciseLaunchButton.isClickable = activeExerciseViewModel.activeButton(
                    binding.root.context,
                    binding,
                    binding.executionField.text.toString(),
                    binding.repetitionField.text.toString(),
                    binding.restField.text.toString(),
                    binding.weightField.text.toString())
            }
        })

        binding.weightField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                binding.exerciseLaunchButton.isClickable = activeExerciseViewModel.activeButton(
                    binding.root.context,
                    binding,
                    binding.executionField.text.toString(),
                    binding.repetitionField.text.toString(),
                    binding.restField.text.toString(),
                    binding.weightField.text.toString())
            }
        })


        binding.restField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                binding.exerciseLaunchButton.isClickable = activeExerciseViewModel.activeButton(
                    binding.root.context,
                    binding,
                    binding.executionField.text.toString(),
                    binding.repetitionField.text.toString(),
                    binding.restField.text.toString(),
                    binding.weightField.text.toString())
            }
        })

        binding.exerciseName.text = intent.getStringExtra("name")
        binding.exerciseDescription.text = intent.getStringExtra("description")


    }
}