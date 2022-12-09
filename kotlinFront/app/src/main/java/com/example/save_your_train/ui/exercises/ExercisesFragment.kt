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

        exercisesViewModel = ViewModelProvider(this)[ExercisesViewModel::class.java]

        // Set Listener
        binding.previousExerciseButton.setOnClickListener {
            exercisesViewModel.previousPage(adapter, binding)
        }
        binding.nextExerciseButton.setOnClickListener {
            exercisesViewModel.nextPage(adapter, binding)
        }
        binding.addExerciseButton.setOnClickListener {
            startActivity(Intent(activity,AddExerciseActivity::class.java))
        }

        // Recycler
        binding.exercisesList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.exercisesList.adapter = adapter

        // Pagination
        exercisesViewModel.setClickable(binding)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        exercisesViewModel.setData(requireContext(), adapter, binding)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}