package com.example.save_your_train.ui.exercises.activeExercise

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.databinding.ActiveExerciseLayoutBinding
import com.example.save_your_train.ui.exercises.launchedExercise.LaunchedExerciseActivity


class ActiveExerciseActivity: AppCompatActivity() {

    private lateinit var binding: ActiveExerciseLayoutBinding
    private lateinit var activeExerciseViewModel:  ActiveExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = ActiveExerciseLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get view model
        activeExerciseViewModel = ViewModelProvider(this)[ActiveExerciseViewModel::class.java]
        activeExerciseViewModel.loadJson(binding.root.context) // Load exercises cases

        // Get parameters
        supportActionBar?.title = intent.getStringExtra("name")
        binding.descriptionContent.text = intent.getStringExtra("description")!!.ifEmpty { "Aucune description" }

        // Set Listeners
        setTextChangedListener(binding.executionField)
        setTextChangedListener(binding.repetitionField)
        setTextChangedListener(binding.restField)
        setTextChangedListener(binding.weightField)
        setTextChangedListener(binding.seriesField)
        binding.exerciseLaunchButton.setOnClickListener { activeExerciseViewModel.onClickLaunchButton() }

        setObserve()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // Private functions

    private fun setObserve() {
        activeExerciseViewModel.isLaunched.observe(this) {
            if (it) startLaunchedExerciseActivity()
        }

        // Launch Button
        activeExerciseViewModel.launchButtonClickable.observe(this) {
            binding.exerciseLaunchButton.isClickable = it
        }
        activeExerciseViewModel.launchButtonAlpha.observe(this) {
            binding.exerciseLaunchButton.alpha = it
        }
    }

    private fun startLaunchedExerciseActivity() {
        val intent = Intent(binding.root.context, LaunchedExerciseActivity::class.java)
        intent.putExtra("name", supportActionBar?.title)
        intent.putExtra("execution", this.binding.executionField.text.toString())
        intent.putExtra("repetition", this.binding.repetitionField.text.toString())
        intent.putExtra("rest", this.binding.restField.text.toString())
        intent.putExtra("weight", this.binding.weightField.text.toString())
        intent.putExtra("series", this.binding.seriesField.text.toString().ifEmpty { "1" })
        binding.root.context.startActivity(intent)
    }

    private fun setTextChangedListener(textField: EditText) {
        textField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (textField.text.toString() == "0") {
                    // User can't write 0
                    textField.setText("")
                    return
                }
                activeExerciseViewModel.onChangeText(
                    binding.executionField.text.toString(),
                    binding.repetitionField.text.toString(),
                    binding.restField.text.toString(),
                    binding.weightField.text.toString()
                )
            }
        })
    }
}