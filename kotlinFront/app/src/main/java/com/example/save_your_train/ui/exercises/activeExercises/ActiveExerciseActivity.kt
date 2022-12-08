package com.example.save_your_train.ui.exercises.activeExercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.databinding.ActiveExerciseBinding
import com.example.save_your_train.disableButton


class ActiveExerciseActivity: AppCompatActivity() {

    private lateinit var binding: ActiveExerciseBinding
    private lateinit var activeExerciseViewModel:  ActiveExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = ActiveExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get parameters
        binding.exerciseName.text = intent.getStringExtra("name")
        binding.exerciseDescription.text = intent.getStringExtra("description")

        activeExerciseViewModel = ViewModelProvider(this)[ActiveExerciseViewModel::class.java]
        activeExerciseViewModel.loadJson(binding.root.context)

        setListener(binding.executionField)
        setListener(binding.repetitionField)
        setListener(binding.restField)
        setListener(binding.weightField)

        disableButton(binding.exerciseLaunchButton, true)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setListener(textField: EditText) {
        textField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if(binding.repetitionField.text.toString() == "0") {
                    binding.repetitionField.setText("")
                    return
                }
                activeExerciseViewModel.activeButton(binding)
            }
        })
    }
}