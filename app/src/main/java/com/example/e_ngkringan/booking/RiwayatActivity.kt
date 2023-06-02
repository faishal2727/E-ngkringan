package com.example.e_ngkringan.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_ngkringan.adapter.RiwayatAdapter
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityRiwayatBinding
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.model.ListBooking
import com.example.e_ngkringan.profil.ProfilActivity
import com.example.e_ngkringan.util.PreferenceFactory

class RiwayatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRiwayatBinding
    private lateinit var riwayatAdapter: RiwayatAdapter
    private lateinit var riwayatViewHolder: RiwayatViewHolder
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRiwayatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        riwayatViewHolder = ViewModelProvider(this)[RiwayatViewHolder::class.java]
        loginViewModel.getToken().observe(this) {
            if (it != null) {
                riwayatViewHolder.getRiwayat("Bearer $it")
            } else {
                Log.d("TOKEN", "Token Null")
            }
        }
        riwayatViewHolder.listBooking.observe(this) {
            setRecyler(it)
        }
        riwayatViewHolder.isLoading.observe(this) {
            showLoading(it)
        }
        btnBack()
    }

    private fun btnBack(){
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    private fun setRecyler(listRiwayat: List<ListBooking>) {
        riwayatAdapter = RiwayatAdapter(listRiwayat, object : RiwayatAdapter.OnCallbackClicked {
            override fun onClicked(data: ListBooking) {
                startActivity(Intent(this@RiwayatActivity, RiwayatDetailActivity::class.java).also {
                    it.putExtra("id", data.id)
                })
            }
        })
        binding.riwayat.apply {
            adapter = riwayatAdapter
            layoutManager = LinearLayoutManager(this@RiwayatActivity)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar8.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}