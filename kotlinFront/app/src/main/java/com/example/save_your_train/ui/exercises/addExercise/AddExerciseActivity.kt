package com.example.save_your_train.ui.exercises.addExercise

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.databinding.AddExerciseLayoutBinding


class AddExerciseActivity : AppCompatActivity() {

    private lateinit var binding: AddExerciseLayoutBinding
    private lateinit var addExerciseViewModel: AddExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = AddExerciseLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get view model
        addExerciseViewModel = ViewModelProvider(this)[AddExerciseViewModel::class.java]

        // Set Listeners
        binding.exerciseAddButton.setOnClickListener {
            val exercise = Exercise(
                binding.exerciseNameField.text.toString(),
                binding.exerciseNameField.text.toString()
            )
            addExerciseViewModel.onClickAddButton(exercise)
        }
        setTextChangedListener(binding.exerciseNameField)

        setObserve()
    }

    override fun onSupportNavigateUp(): Boolean {
        // Function called when we click on back button
        finish()
        return true
    }

    // Set private functions

    private fun setObserve() {
        addExerciseViewModel.isFinished.observe(this) {
            if (it) finish()
        }

        // Add Button
        addExerciseViewModel.addButtonClickable.observe(this) {
            binding.exerciseAddButton.isClickable = it
        }
        addExerciseViewModel.addButtonAlpha.observe(this) {
            binding.exerciseAddButton.alpha = it
        }

        // Error Text View
        addExerciseViewModel.textError.observe(this) {
            binding.addExerciseError.text = it
        }
        addExerciseViewModel.visibilityError.observe(this) {
            binding.addExerciseError.visibility = it
        }
    }

    private fun setTextChangedListener(textField: EditText) {
        textField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                addExerciseViewModel.onChangeTextName(binding.exerciseNameField.text.toString())
            }
        })
    }
}
