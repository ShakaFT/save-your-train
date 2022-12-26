package com.example.save_your_train.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.save_your_train.data.History
import com.example.save_your_train.databinding.HistoryItemLayoutBinding
import com.example.save_your_train.ui.history.activeHistory.ActiveHistoryActivity
import java.text.SimpleDateFormat
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class HistoryListAdapter: RecyclerView.Adapter<HistoryListAdapter.HistoryItemViewHolder>() {

    private var listHistories: MutableList<History> = mutableListOf()

    fun fillHistories(histories: MutableList<History>) {
        listHistories = histories
        notifyDataSetChanged()
    }

    class HistoryItemViewHolder(private val binding: HistoryItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(history: History) {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.FRANCE)
            val date = dateFormat.format(Date(history.dateMs.toLong()))

            binding.historyName.text = history.name
            binding.date.text = date
            binding.historyItem.setOnClickListener{
                // Go to Active History Activity
                val intent = Intent(binding.root.context, ActiveHistoryActivity::class.java)
                intent.putExtra("name", history.name)
                intent.putExtra("dateMs", history.dateMs.toDouble())
                intent.putExtra("execution", history.execution)
                intent.putExtra("rest", history.rest)
                intent.putExtra("repetition", history.repetition)
                intent.putExtra("weight", history.weight)
                intent.putExtra("series", history.series)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(
            HistoryItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return listHistories.size
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(listHistories[position])
    }
}
