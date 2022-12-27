package com.example.save_your_train.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AccountDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel : ViewModel() {

    val isSignedOut = MutableLiveData<Boolean>(false)

    fun onClickSignOutButton(accountDataStore: AccountDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            accountDataStore.setAccount(AccountModel("", firstName = "", lastName = ""))
            isSignedOut.postValue(true)
        }
    }
}