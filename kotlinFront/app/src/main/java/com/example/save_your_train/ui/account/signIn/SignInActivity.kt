package com.example.save_your_train.ui.account.signIn

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.save_your_train.MainActivity
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.databinding.SignInLayoutBinding
import com.example.save_your_train.getPopup
import com.example.save_your_train.ui.account.signUp.SignUpActivity
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.launch

class SignInActivity: AppCompatActivity() {

    private lateinit var binding: SignInLayoutBinding
    private lateinit var signInViewModel: SignInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = SignInLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get view model
        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]

        // Set Listeners
        binding.signInButton.setOnClickListener {
            signInViewModel.onClickSignInButton(AccountDataStore(baseContext), AccountModel(
                binding.emailSignIn.text.toString(),
                password = binding.passwordSignIn.text.toString()
            ))
        }
        binding.linkSignUp.setOnClickListener {
            binding.root.context.startActivity(Intent(binding.root.context, SignUpActivity::class.java))
        }

        setObserve()
    }

    override fun onBackPressed() {
        getPopup(
            this,
            "Voulez-vous quitter l'application ?",
            "",
            positiveFunction = { moveTaskToBack(true); },
        ).show()
    }

    // Set private functions

    private fun setObserve() {
        signInViewModel.isSignedIn.observe(this) {
            if (it) {
                binding.root.context.startActivity(Intent(binding.root.context, MainActivity::class.java))
                finish()
            }
        }

        // Sign In Button
        signInViewModel.signInButtonClickable.observe(this) {
            binding.signInButton.isClickable = it
        }
        signInViewModel.signInButtonAlpha.observe(this) {
            binding.signInButton.alpha = it
        }

        // Error Text View
        signInViewModel.textError.observe(this) {
            binding.signInError.text = it
        }
        signInViewModel.visibilityError.observe(this) {
            binding.signInError.visibility = it
        }
    }
}
