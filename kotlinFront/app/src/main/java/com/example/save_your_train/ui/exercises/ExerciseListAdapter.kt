package com.example.save_your_train.ui.exercises

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.databinding.ExerciseItemLayoutBinding
import com.example.save_your_train.ui.exercises.activeExercises.ActiveExerciseActivity


class ExerciseListAdapter: RecyclerView.Adapter<ExerciseListAdapter.ExerciseItemViewHolder>() {

    private var listExercises: MutableList<Exercise> = mutableListOf()

    fun fillExercises(exercises: MutableList<Exercise>) {
        listExercises = exercises
    }

    class ExerciseItemViewHolder(private val binding: ExerciseItemLayoutBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(exercise: Exercise) {
            binding.exerciseName.text = exercise.name
            binding.exerciseDescription.text = exercise.description
            binding.exerciseItem.setOnClickListener{
                val intent = Intent(binding.root.context,ActiveExerciseActivity::class.java)
                Toast.makeText(binding.root.context,"bonjour", Toast.LENGTH_SHORT)
                intent.putExtra("name", this.binding.exerciseName.text.toString())
                intent.putExtra("description", this.binding.exerciseDescription.text.toString())
                binding.root.context.startActivity(intent)
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

