package com.example.e_ngkringan.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.R
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityRegisterBinding
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.login.MainActivity
import com.example.e_ngkringan.util.PreferenceFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        loginViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        doRegister()
        btnBack()
        toLogin()


    }

    private fun toLogin() {
        binding.toLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }


    private fun btnBack() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun doRegister() {
        binding.btnRegister.setOnClickListener {
            val name = binding.edtUsernameRegister.text.toString().trim()
            val email = binding.edtEmailRegister.text.toString().trim()
            val password = binding.edtPasswordRegister.text.toString().trim()
            when{
                name.isEmpty() -> {
                    binding.edtUsernameRegister.error = "Masukan Username"
                }
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
                    loginViewModel.doSignUp(name, email, password)
                    loginViewModel.signUpObserver().observe(this) {
                        if (it != null) {
                            startActivity(Intent(this, MainActivity::class.java))

                        } else {
                            Toast.makeText(this, "bakso", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}