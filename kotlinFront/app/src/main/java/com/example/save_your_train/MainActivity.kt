package com.example.save_your_train

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.save_your_train.data.AccountDataStore
import com.example.save_your_train.data.AppDatabase
import com.example.save_your_train.data.Exercise
import com.example.save_your_train.data.JsonData
import com.example.save_your_train.databinding.ActivityMainBinding
import com.example.save_your_train.ui.account.signIn.SignInActivity
import com.example.save_your_train.ui.account.signUp.SignUpActivity
import com.example.save_your_train.ui.profile.AccountModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData() // Set data when app is launched

        // Setup title of each fragment
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_exercises, R.id.navigation_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
            val currentAccount = AccountDataStore(baseContext).getAccount.first() ?: AccountModel("")
            if (currentAccount.email.isEmpty()) {
                // Display Sign Up Page
                binding.root.context.startActivity(Intent(binding.root.context, SignInActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        getPopup(
            this,
            "Voulez-vous quitter l'application ?",
            "",
            positiveFunction = { moveTaskToBack(true); },
        ).show()
    }
    
    private fun initData() {
        AppDatabase.setDatabase(baseContext)
        JsonData.loadJsonData(baseContext)
    }
}