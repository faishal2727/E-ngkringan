package com.example.e_ngkringan.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityRiwayatDetailBinding
import com.example.e_ngkringan.detail.DetailViewModel
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.profil.ProfilActivity
import com.example.e_ngkringan.profil.ProfilViewModel
import com.example.e_ngkringan.util.PreferenceFactory

class RiwayatDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatDetailBinding
    private lateinit var riwayatDetailViewModel: RiwayatDetailViewModel
    private lateinit var profilViewModel: ProfilViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        riwayatDetailViewModel = ViewModelProvider(this)[RiwayatDetailViewModel::class.java]
        profilViewModel = ViewModelProvider(this)[ProfilViewModel::class.java]

        getDetail()
        setData()
        showDetail()
        back()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.root
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar8.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, RiwayatActivity::class.java))
            finish()
        }
    }

    private fun getDetail() {
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
                    tvNameUser.text = it.name
                }
            }
        }
    }

    private fun showDetail() {
        val id = intent.getIntExtra("id", 1)
        riwayatDetailViewModel = ViewModelProvider(this)[RiwayatDetailViewModel::class.java]
        riwayatDetailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        if (id != null) {
            riwayatDetailViewModel.getDetail(id)
        }
        riwayatDetailViewModel.detailBooking.observe(this) {
            if (it != null) {
                binding.apply {
                    tvNote.text = it.note
                    tvDate.text = it.dates
                    tvDateCreate.text = it.createdAt
                    tvUserCode.text = it.id.toString()
                    tvRestoCode.text = it.resto_code
                }
            }
        }
    }
}