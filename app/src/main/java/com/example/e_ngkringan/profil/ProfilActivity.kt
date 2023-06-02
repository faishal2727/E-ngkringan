package com.example.e_ngkringan.profil

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.booking.RiwayatActivity
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityProfilBinding
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.login.MainActivity
import com.example.e_ngkringan.util.PreferenceFactory

class ProfilActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilBinding
    private lateinit var profilViewModel: ProfilViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        profilViewModel = ViewModelProvider(this)[ProfilViewModel::class.java]
        profilViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        getProfile()
        setData()
        doLogout()
        btnBack()
        toRiwayat()
    }

    private fun btnBack() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun getProfile() {
        loginViewModel.getToken().observe(this) {
            if (it != null) {
                profilViewModel.getUser("Bearer $it")
            } else {
                Log.d("TOKEN", "Token Null")
            }
        }
    }

    private fun setData() {
        profilViewModel.getUser.observe(this) {
            if (it != null) {
                binding.apply {
                    tvNameProfil.text = it.name
                    tvEmailProfil.text = it.email
                    tvPasswordProfil.text = it.password
                }
            }
        }
    }

    private fun doLogout() {
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).also {
                loginViewModel.deleteToken()
            })
        }
    }

    private fun toRiwayat() {
        binding.cardView.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar6.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}