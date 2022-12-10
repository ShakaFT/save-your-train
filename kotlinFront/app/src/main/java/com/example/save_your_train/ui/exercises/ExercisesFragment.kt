package com.example.save_your_train.ui.exercises

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.save_your_train.databinding.FragmentExercisesBinding
import com.example.save_your_train.ui.exercises.addExercise.AddExerciseActivity


class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private val binding: FragmentExercisesBinding get() = _binding!!
    private val adapter: ExerciseListAdapter = ExerciseListAdapter()

    private lateinit var exercisesViewModel: ExercisesViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)

        // Get view model
        exercisesViewModel = ViewModelProvider(requireActivity())[ExercisesViewModel::class.java]

        // Set Listener
        binding.previousExerciseButton.setOnClickListener {
            exercisesViewModel.previousPage()
        }
        binding.nextExerciseButton.setOnClickListener {
            exercisesViewModel.nextPage()
        }
        binding.addExerciseButton.setOnClickListener {
            startActivity(Intent(activity, AddExerciseActivity::class.java))
        }

        // Recycler
        binding.exercisesList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.exercisesList.adapter = adapter

        setObserve()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        exercisesViewModel.refreshRecycler()
    }

    // Private functions

    private fun setObserve() {
        // previous page button
        exercisesViewModel.previousButtonClickable.observe(requireActivity()) {
            binding.previousExerciseButton.isClickable = it
        }
        exercisesViewModel.previousButtonAlpha.observe(requireActivity()) {
            binding.previousExerciseButton.alpha = it
        }

        // next page button
        exercisesViewModel.nextButtonClickable.observe(requireActivity()) {
            binding.nextExerciseButton.isClickable = it
        }
        exercisesViewModel.nextButtonAlpha.observe(requireActivity()) {
            binding.nextExerciseButton.alpha = it
        }

        // Recycler
        exercisesViewModel.exercisesList.observe(requireActivity()) {
            adapter.fillExercises(it)
        }
    }
}