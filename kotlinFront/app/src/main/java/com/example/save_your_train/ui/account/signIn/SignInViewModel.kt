package com.example.save_your_train.ui.account.signIn

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.data.signInRemote
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.*

class SignInViewModel: ViewModel() {

    val isSignedIn = MutableLiveData<Boolean>(false)

    val signInButtonClickable = MutableLiveData<Boolean>()
    val signInButtonAlpha = MutableLiveData<Float>()
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    // Public functions

    fun onClickSignInButton(accountDataStore: AccountDataStore, account: AccountModel) {
        if (!checkEmail(account.email) || !checkPassword(account.password)) {
            displayError(true, "L'email ou le mot de passe est incorrect")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            disableSignInButton(true)
            if (signIn(accountDataStore, account)) {
                isSignedIn.postValue(true)
            } else {
                disableSignInButton(false)
            }
        }
    }

    // Private methods

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPassword(password: String): Boolean {
        return password.length >= 8
    }

    private fun disableSignInButton(disabled: Boolean) {
        signInButtonClickable.postValue(!disabled)
        signInButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun signIn(accountDataStore: AccountDataStore, account: AccountModel): Boolean {
        val worked = GlobalScope.async {
            try {
                if (signInRemote(accountDataStore, account)) {
                    true
                } else {
                    displayError(true, "L'email ou le mot de passe est incorrect")
                    false
                }
            } catch (e: Exception) {
                displayError(true, "Une erreur est survenue, veuillez réessayer plus tard...")
                false
            }
        }
        return worked.await()
    }
}