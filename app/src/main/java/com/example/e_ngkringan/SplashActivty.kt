package com.example.e_ngkringan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivitySplashActivtyBinding
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.login.MainActivity
import com.example.e_ngkringan.menu.ActivityMenu
import com.example.e_ngkringan.util.PreferenceFactory

class SplashActivty : AppCompatActivity() {
    private lateinit var binding: ActivitySplashActivtyBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            isLogin()
        }, 2500)
    }

    private fun isLogin() {
        loginViewModel.getToken().observe(this) {
            if (it != null) {
                if (!it.equals("undefined")) {
                    startActivity(Intent(this, HomeActivity::class.java))
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } else {
                Toast.makeText(this, "Token Null", Toast.LENGTH_SHORT).show()
            }
        }

    }
}