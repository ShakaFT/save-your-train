package com.example.save_your_train.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.save_your_train.databinding.FragmentExercisesBinding

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null
    private lateinit var exercisesViewModel:ExercisesViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = ExerciseListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        exercisesViewModel = ViewModelProvider(this)[ExercisesViewModel::class.java]

        // Set Listener
        binding.previousExerciseButton.setOnClickListener { previousPage() }
        binding.nextExerciseButton.setOnClickListener { nextPage() }
        binding.addExerciseButton.setOnClickListener { addExercise() }

        //RECYCLER
        binding.exercisesList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.exercisesList.adapter = adapter

        adapter.fillExercises(exercisesViewModel.displayExercises)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addExercise() {
        exercisesViewModel.addExercise(Exercise("coucou", "salut"))
        adapter.fillExercises(exercisesViewModel.exercises)
    }

    private fun previousPage() {
        exercisesViewModel.previousPage(adapter)
    }

    private fun nextPage() {
        exercisesViewModel.nextPage(adapter)
    }
}