package com.example.save_your_train.ui.account.signUp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.save_your_train.MainActivity
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.databinding.SignUpLayoutBinding
import com.example.save_your_train.ui.account.signIn.SignInActivity
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignUpActivity: AppCompatActivity() {
    private lateinit var binding: SignUpLayoutBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = SignUpLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get view model
        signUpViewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        // Set Listeners
        binding.signUpButton.setOnClickListener {
            signUpViewModel.onClickSignUpButton(AccountDataStore(baseContext), AccountModel(
                email = binding.emailSignUp.text.toString(),
                firstName = binding.firstNameSignUp.text.toString(),
                lastName = binding.lastNameSignUp.text.toString(),
                password = binding.passwordSignUp1.text.toString(),
                confirmationPassword = binding.passwordSignUp2.text.toString(),
            ))
        }
        binding.linkSignIn.setOnClickListener {
            finish()
        }

        setObserve()
    }

    // Set private functions

    private fun setObserve() {
        signUpViewModel.isSignedUp.observe(this) {
            if (it) {
                binding.root.context.startActivity(Intent(binding.root.context, MainActivity::class.java))
            }
        }
    }
}