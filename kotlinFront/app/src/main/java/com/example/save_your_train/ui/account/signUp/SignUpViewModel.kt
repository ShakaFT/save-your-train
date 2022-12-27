package com.example.save_your_train.ui.account.signUp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {

    val isSignedUp = MutableLiveData<Boolean>(false)

    // Public functions

    fun onClickSignUpButton(accountDataStore: AccountDataStore, account: AccountModel) {
        if (!checkEmail(account.email)) return

        val errorPassword = getErrorPassword(account.password, account.confirmationPassword)
        if (errorPassword.isNotEmpty()) return

        CoroutineScope(Dispatchers.IO).launch {
            accountDataStore.setAccount(account)
            isSignedUp.postValue(true)
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
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
}