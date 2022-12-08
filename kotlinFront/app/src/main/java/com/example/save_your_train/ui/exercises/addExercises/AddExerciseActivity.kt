package com.example.save_your_train.ui.exercises.addExercises

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.databinding.AddExerciseLayoutBinding
import com.example.save_your_train.disableButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddExerciseActivity : AppCompatActivity() {

    private lateinit var binding: AddExerciseLayoutBinding
    private lateinit var addExerciseViewModel: AddExerciseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddExerciseLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addExerciseViewModel = ViewModelProvider(this)[AddExerciseViewModel::class.java]

        // Set Listener
        binding.exerciseAddButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                insertExerciseDb()
                finish() // This function returns the user on the previous page/activity
            }
        }
        setListener(binding.exerciseNameField)
        disableButton(binding.exerciseAddButton, true)
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
                addExerciseViewModel.activeButton(binding)
            }
        })
    }

    private fun insertExerciseDb() {
        val db = AppDatabase.getDatabase(this)
        val exerciseDao = db.exerciseDao()
        exerciseDao.insertAll(
            Exercise(
                binding.exerciseNameField.text.toString(),
                binding.exerciseDescriptionField.text.toString()
            )
        )
    }
}
