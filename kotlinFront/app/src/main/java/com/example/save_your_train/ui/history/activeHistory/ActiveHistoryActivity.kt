package com.example.save_your_train.ui.history.activeHistory

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.History
import com.example.save_your_train.databinding.ActiveHistoryLayoutBinding
import com.example.save_your_train.getPopup
import com.example.save_your_train.ui.exercises.activeExercise.ActiveExerciseViewModel
import java.text.SimpleDateFormat
import java.util.*

class ActiveHistoryActivity: AppCompatActivity() {

    private lateinit var binding: ActiveHistoryLayoutBinding
    private lateinit var activeHistoryViewModel: ActiveHistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = ActiveHistoryLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get view model
        activeHistoryViewModel = ViewModelProvider(this)[ActiveHistoryViewModel::class.java]

        // Get Parameters
        supportActionBar?.title = intent.getStringExtra("name")

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.FRANCE)
        val hourFormat = SimpleDateFormat("HH:mm", Locale.FRANCE)
        binding.date.text = dateFormat.format(Date(intent.getDoubleExtra("dateMs", 0.0).toLong()))
        binding.hour.text = hourFormat.format(Date(intent.getDoubleExtra("dateMs", 0.0).toLong()))

        binding.execution.text = intent.getStringExtra("execution")
        binding.repetition.text = intent.getStringExtra("repetition")
        binding.rest.text = intent.getStringExtra("rest")
        binding.weight.text = intent.getStringExtra("weight")
        binding.series.text = intent.getStringExtra("series")

        // Set Listeners
        binding.removeHistoryButton.setOnClickListener { getAlertPopup().show() }


        // Set Display
        displayTextView(binding.execution.text.isNotEmpty(), binding.historyExecutionLine)
        displayTextView(binding.repetition.text.isNotEmpty(), binding.historyRepetitionLine)
        displayTextView(binding.rest.text.isNotEmpty(), binding.historyRestLine)
        displayTextView(binding.weight.text.isNotEmpty(), binding.historyWeightLine)
        displayTextView(binding.series.text.isNotEmpty(), binding.historySeriesLine)

        setObserve()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun setObserve() {
        activeHistoryViewModel.isFinished.observe(this) {
            println("gooooooo")
            println(it)
            if (it) finish()
        }

        // Error Text View
        activeHistoryViewModel.textError.observe(this) {
            binding.activeHistoryError.text = it
        }
        activeHistoryViewModel.visibilityError.observe(this) {
            binding.activeHistoryError.visibility = it
        }

        // Delete Button
        activeHistoryViewModel.deleteButtonClickable.observe(this) {
            binding.removeHistoryButton.isClickable = it
        }
        activeHistoryViewModel.deleteButtonAlpha.observe(this) {
            binding.removeHistoryButton.alpha = it
        }
    }

    private fun displayTextView(displayed: Boolean, tableRow: TableRow) {
        tableRow.visibility = if(displayed) View.VISIBLE else View.GONE
    }

    private fun getAlertPopup(): AlertDialog.Builder {
        return getPopup(
            this,
            "Supprimer l'exercise ${supportActionBar?.title} de l'historique ?",
            "Cette action est irréversible.",
            positiveFunction = {
                activeHistoryViewModel.onClickRemoveHistory(
                    History(
                        intent.getDoubleExtra("dateMs", 0.0),
                        supportActionBar?.title.toString(),
                        binding.execution.text.toString(),
                        binding.repetition.text.toString(),
                        binding.rest.text.toString(),
                        binding.series.text.toString(),
                        binding.weight.text.toString()
                    )
                )
            },
        )
    }
}