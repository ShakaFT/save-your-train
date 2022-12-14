package com.example.save_your_train.ui.history.activeHistory

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import androidx.appcompat.app.AppCompatActivity
import com.example.save_your_train.databinding.ActiveHistoryLayoutBinding
import java.text.SimpleDateFormat
import java.util.*

class ActiveHistoryActivity: AppCompatActivity() {

    private lateinit var binding: ActiveHistoryLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = ActiveHistoryLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Showing the back button in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Get Parameters
        supportActionBar?.title = intent.getStringExtra("name")

        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.FRANCE)
        val hourFormat = SimpleDateFormat("HH:mm", Locale.FRANCE)
        binding.date.text = dateFormat.format(Date(intent.getLongExtra("dateMs", 0)))
        binding.hour.text = hourFormat.format(Date(intent.getLongExtra("dateMs", 0)))

        binding.execution.text = intent.getStringExtra("execution")
        binding.repetition.text = intent.getStringExtra("repetition")
        binding.rest.text = intent.getStringExtra("rest")
        binding.weight.text = intent.getStringExtra("weight")
        binding.series.text = intent.getStringExtra("series")

        //Set Display
        displayTextView(binding.execution.text.isNotEmpty(), binding.historyExecutionLine)
        displayTextView(binding.repetition.text.isNotEmpty(), binding.historyRepetitionLine)
        displayTextView(binding.rest.text.isNotEmpty(), binding.historyRestLine)
        displayTextView(binding.weight.text.isNotEmpty(), binding.historyWeightLine)
        displayTextView(binding.series.text.isNotEmpty(), binding.historySeriesLine)

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun displayTextView(displayed: Boolean, tableRow: TableRow) {
        tableRow.visibility = if(displayed) View.VISIBLE else View.GONE
    }

}