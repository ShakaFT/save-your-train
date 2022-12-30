package com.example.save_your_train.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.databinding.FragmentProfileBinding
import com.example.save_your_train.ui.account.signIn.SignInActivity
import com.example.save_your_train.ui.profile.maps.MapsActivity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val accountDataStore = AccountDataStore(requireContext())

        binding.signUpButton.setOnClickListener {
            profileViewModel.onClickSignOutButton(accountDataStore)
        }
        binding.goToMap.setOnClickListener {
            onClickMap()
        }

        setAccountData(accountDataStore)
        setObserve()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setAccountData(accountDataStore: AccountDataStore) {
        viewLifecycleOwner.lifecycleScope.launch {
            val account: AccountModel = accountDataStore.getAccount.first()!!
            binding.currentEmail.text = account.email
            binding.currentFirstName.text = account.firstName
            binding.currentLastName.text = account.lastName
        }
    }

    private fun setObserve() {
        profileViewModel.isSignedOut.observe(requireActivity()) {
            if (it) binding.root.context.startActivity(Intent(
                binding.root.context, SignInActivity::class.java
            ))
        }
    }

    private fun onClickMap() {
        binding.root.context.startActivity(Intent(
            binding.root.context, MapsActivity::class.java
        ))
    }
}