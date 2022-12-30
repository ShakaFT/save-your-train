package com.example.save_your_train.ui.account.signUp

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.alphaAble
import com.example.save_your_train.alphaDisable
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.data.addRemoteAccount
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.*

class SignUpViewModel: ViewModel() {

    val isSignedUp = MutableLiveData<Boolean>(false)

    val signUpButtonClickable = MutableLiveData<Boolean>()
    val signUpButtonAlpha = MutableLiveData<Float>()
    val textError = MutableLiveData<String>()
    val visibilityError = MutableLiveData<Int>(View.GONE)

    // Public functions

    fun onClickSignUpButton(accountDataStore: AccountDataStore, account: AccountModel) {
        if (account.email.isEmpty() || account.password.isEmpty() || account.firstName.isEmpty() || account.lastName.isEmpty()) {
            displayError(true, "Tous les champs sont obligatoires")
            return
        }
        if (!checkEmail(account.email)) {
            displayError(true, "L'email n'est pas valide")
            return
        }

        val errorPassword = getErrorPassword(account.password, account.confirmationPassword)
        if (errorPassword.isNotEmpty()) {
            displayError(true, errorPassword)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            disableSignUpButton(true)
            if (signUp(accountDataStore, account)) {
                isSignedUp.postValue(true)
            } else {
                disableSignUpButton(false)
            }
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun disableSignUpButton(disabled: Boolean) {
        signUpButtonClickable.postValue(!disabled)
        signUpButtonAlpha.postValue(if (disabled) alphaDisable else alphaAble)
    }

    private fun displayError(display: Boolean, text: String = "") {
        textError.postValue(text)
        visibilityError.postValue(if (display) View.VISIBLE else View.GONE)
    }

    private fun getErrorPassword(password1: String, password2: String): String {
        if (password1 != password2) {
            return "Les mots de passes doivent être identiques"
        }
        if (password1.length < 8) {
            return "Le mot de passe doit contenir au moins 8 caractères"
        }
        return ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    private suspend fun signUp(accountDataStore: AccountDataStore, account: AccountModel): Boolean {
        val worked = GlobalScope.async {
            try {
                addRemoteAccount(account)
                accountDataStore.setAccount(account)
                true
            } catch (e: Exception) {
                displayError(true, "Un compte est déjà associé à cet email")
                false
            }
        }
        return worked.await()
    }
}