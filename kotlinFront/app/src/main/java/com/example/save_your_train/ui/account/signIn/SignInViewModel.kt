package com.example.save_your_train.ui.account.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignInViewModel: ViewModel() {

    val isSignedIn = MutableLiveData<Boolean>(false)

    // Public functions

    fun onClickSignInButton(accountDataStore: AccountDataStore, account: AccountModel) {
        if (!checkEmail(account.email)) return
        if (!checkPassword(account.password)) return

        CoroutineScope(Dispatchers.IO).launch {
            accountDataStore.setAccount(account)
            isSignedIn.postValue(true)
        }
    }

    private fun checkEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkPassword(password: String): Boolean {
        return password.length >= 8
    }
}