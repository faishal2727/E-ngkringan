package com.example.e_ngkringan.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.R
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityMainBinding
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.menu.ActivityMenu
import com.example.e_ngkringan.register.RegisterActivity
import com.example.e_ngkringan.util.PreferenceFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        login()
        register()
    }

    private fun login() {
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString().trim()
            val password = binding.edtPasswordRegister.text.toString().trim()

            when{
                email.isEmpty()->{
                    binding.edtEmailRegister.error = "Masukan Email"
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.edtEmailRegister.error = "Email Tidak Valid"
                }
                password.isEmpty() -> {
                    binding.edtPasswordRegister.error = "Masukan Password"
                }
                password.length <6 -> {
                    binding.edtPasswordRegister.error = "Password Minimal 6 Karakter"
                }
                else -> {
                    userLoginPreferences = UserLoginPreferences(this)
                    loginViewModel = ViewModelProvider(
                        this,
                        PreferenceFactory(userLoginPreferences)
                    )[LoginViewModel::class.java]
                    loginViewModel.login(email, password)
                    loginViewModel.usersLogin.observe(this) {
                    loginViewModel.setToken(it.token)
                    startActivity(Intent(this, HomeActivity::class.java))
                    Toast.makeText(this, "Selamat Datang" + " " + it.name, Toast.LENGTH_SHORT).show()
                }
             }
            }
        }
    }

    private fun register() {
        binding.toRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}