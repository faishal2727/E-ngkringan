package com.example.e_ngkringan.booking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.e_ngkringan.databinding.ActivitySuccessBookingBinding
import com.example.e_ngkringan.home.HomeActivity

class SuccessBookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessBookingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}