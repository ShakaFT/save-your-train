package com.example.save_your_train.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.save_your_train.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding get() = _binding!!
    private val adapter: HistoryListAdapter = HistoryListAdapter()

    private lateinit var historyViewModel: HistoryViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Get view model
        historyViewModel = ViewModelProvider(requireActivity())[HistoryViewModel::class.java]

        // Set Listener
        binding.previousHistoryButton.setOnClickListener {
            historyViewModel.previousPage()
        }
        binding.nextHistoryButton.setOnClickListener {
            historyViewModel.nextPage()
        }

        // Recycler
        binding.historiesList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
        binding.historiesList.adapter = adapter

        setObserve()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        historyViewModel.refreshRecycler()
    }

    // Private functions

    private fun setObserve() {
        // previous page button
        historyViewModel.previousButtonClickable.observe(requireActivity()) {
            binding.previousHistoryButton.isClickable = it
        }
        historyViewModel.previousButtonAlpha.observe(requireActivity()) {
            binding.previousHistoryButton.alpha = it
        }

        // next page button
        historyViewModel.nextButtonClickable.observe(requireActivity()) {
            binding.nextHistoryButton.isClickable = it
        }
        historyViewModel.nextButtonAlpha.observe(requireActivity()) {
            binding.nextHistoryButton.alpha = it
        }

        // Recycler
        historyViewModel.historiesList.observe(requireActivity()) {
            adapter.fillHistories(it)
        }
    }
}