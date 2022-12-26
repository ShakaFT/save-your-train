package com.example.save_your_train.data;

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/* val dataStore = DataStoreManager(context)
lifecycleScope.launch {
    println("current email :" + dataStore.getAccount.first())
}
lifecycleScope.launch {
    dataStore.setAccount(...)
} */

public class AccountDataStore(private val context: Context) {

    // to make sure there is only one instance
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="account")
        val EMAIL_KEY = stringPreferencesKey("email")
        val FIRST_NAME_KEY = stringPreferencesKey("firstName")
        val LAST_NAME_KEY = stringPreferencesKey("lastName")
    }

    // to get the account
    val getAccount: Flow<AccountModel?> = context.dataStore.data
        .map { preferences ->
            AccountModel(
                email = preferences[EMAIL_KEY] ?: "",
                firstName = preferences[FIRST_NAME_KEY] ?: "",
                lastName = preferences[LAST_NAME_KEY] ?: "",
            )
        }

    // to save the account
    suspend fun setAccount(account: AccountModel) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = account.email
            preferences[FIRST_NAME_KEY] = account.firstName
            preferences[LAST_NAME_KEY] = account.lastName
        }
    }
}
