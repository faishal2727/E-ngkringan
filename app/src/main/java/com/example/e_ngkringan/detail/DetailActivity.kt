package com.example.e_ngkringan.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.e_ngkringan.adapter.MenuAdapter
import com.example.e_ngkringan.booking.BookingActivity
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityDetailBinding
import com.example.e_ngkringan.databinding.ActivityMenuBinding
import com.example.e_ngkringan.home.HomeActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.menu.ActivityMenu
import com.example.e_ngkringan.model.AngkringanDetail
import com.example.e_ngkringan.model.DetailMenu
import com.example.e_ngkringan.profil.ProfilViewModel
import com.example.e_ngkringan.util.PreferenceFactory

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel
    private lateinit var profilViewModel: ProfilViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        profilViewModel = ViewModelProvider(this)[ProfilViewModel::class.java]
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        showDetail()
        back()
    }

    private fun showMenu(code: String) {
        binding.cardMenu.setOnClickListener {
            startActivity(Intent(this, ActivityMenu::class.java).also {
                it.putExtra("code", code)
            })
        }
    }

    private fun booking(code: String) {
        binding.cardBooking.setOnClickListener {
            startActivity(Intent(this, BookingActivity::class.java).also {
                it.putExtra("resto", code)
                Log.d("kkkk", code)
            })
        }
    }

    private fun showDetail() {
        val id = intent.getIntExtra("id", 1)
        detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        if (id != null) {
            detailViewModel.getDetailAngkringan(id)
        }
        detailViewModel.angkringanDetail.observe(this) {
            if (it != null) {
                binding.apply {
                    tvNameDetail.text = it.name
                    tvAddressDetail.text = it.address
                    tvTotalTableDetail.text = it.total_table.toString()
                    tvDescc.text = it.desc
                    Glide.with(binding.ivAngkringanDetail)
                        .load(it.image)
                        .into(ivAngkringanDetail)
                    showMenu(it.code)
                    booking(it.code)
                    val menu = it.menu.size
                    tvTotalMenu.text = menu.toString()
                }
            }
        }
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this@DetailActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}