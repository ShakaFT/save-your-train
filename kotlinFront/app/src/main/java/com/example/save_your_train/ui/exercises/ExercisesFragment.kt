package com.example.save_your_train.ui.exercises

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.save_your_train.databinding.FragmentExercisesBinding

class ExercisesFragment : Fragment() {

    private var _binding: FragmentExercisesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val exercisesViewModel =
            ViewModelProvider(this).get(ExercisesViewModel::class.java)

        _binding = FragmentExercisesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textExercises
        exercisesViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}