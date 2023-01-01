package com.example.save_your_train.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileViewModel : ViewModel() {

    val isSignedOut = MutableLiveData<Boolean>(false)

    fun onClickSignOutButton(accountDataStore: AccountDataStore) {
        CoroutineScope(Dispatchers.IO).launch {
            // Delete user data
            accountDataStore.setAccount(AccountModel("", firstName = "", lastName = ""))
            // Delete exercises
            AppDatabase.data!!.exerciseDao().deleteAll()
            // Delete histories
            AppDatabase.data!!.historyDao().deleteAll()

            isSignedOut.postValue(true)
        }
    }
}