package com.example.save_your_train.ui.exercises.addExercises


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.databinding.AddExerciseLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddExerciseActivity : AppCompatActivity() {

    private lateinit var binding: AddExerciseLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AddExerciseLayoutBinding.inflate(layoutInflater)

        binding.exerciseAddButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                callDb()
                finish()
            }
        }
        setContentView(binding.root)
    }

    private fun callDb() {
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
