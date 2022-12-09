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
        binding.exerciseLaunchButton.setOnClickListener {
            val intent = Intent(binding.root.context, LaunchedExerciseActivity::class.java)
            intent.putExtra("name", this.binding.exerciseName.text.toString())
            intent.putExtra("execution", this.binding.executionField.text.toString())
            intent.putExtra("repetition", this.binding.repetitionField.text.toString())
            intent.putExtra("rest", this.binding.restField.text.toString())
            intent.putExtra("weight", this.binding.weightField.text.toString())
            intent.putExtra("series", this.binding.seriesField.text.toString())
            binding.root.context.startActivity(intent)
        }

        //disableButton(binding.exerciseLaunchButton, true)
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