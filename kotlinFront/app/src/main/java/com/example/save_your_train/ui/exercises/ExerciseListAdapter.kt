package com.example.save_your_train.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.save_your_train.databinding.ExerciseItemLayoutBinding


class ExerciseListAdapter: RecyclerView.Adapter<ExerciseListAdapter.ExerciseItemViewHolder>() {

    private var listExercises: MutableList<Exercise> = mutableListOf<Exercise>()

    fun fillExercises(exercises: MutableList<Exercise>) {
        listExercises = exercises
        notifyDataSetChanged()
    }

    class ExerciseItemViewHolder(private val binding: ExerciseItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(exercise: Exercise) {
            binding.exerciseName.text = exercise.name
            binding.exerciseDescription.text = exercise.description
            binding.exerciseItem.setOnClickListener{
                Toast.makeText(binding.root.context, "ça marche", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseItemViewHolder {
        return ExerciseItemViewHolder(ExerciseItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return listExercises.size
    }

    override fun onBindViewHolder(holder: ExerciseItemViewHolder, position: Int) {
        holder.bind(listExercises[position])
    }
}

