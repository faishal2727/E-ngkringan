package com.example.e_ngkringan.booking

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.e_ngkringan.R
import com.example.e_ngkringan.StringHelper
import com.example.e_ngkringan.data_store.UserLoginPreferences
import com.example.e_ngkringan.databinding.ActivityBookingBinding
import com.example.e_ngkringan.detail.DetailActivity
import com.example.e_ngkringan.login.LoginViewModel
import com.example.e_ngkringan.login.MainActivity
import com.example.e_ngkringan.model.DetailBooking
import com.example.e_ngkringan.profil.ProfilViewModel
import com.example.e_ngkringan.util.PreferenceFactory
import java.util.*

class BookingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookingBinding
    private lateinit var bookingViewModel: BookingViewModel
    private lateinit var profilViewModel: ProfilViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var userLoginPreferences: UserLoginPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userLoginPreferences = UserLoginPreferences(this)
        loginViewModel = ViewModelProvider(
            this,
            PreferenceFactory(userLoginPreferences)
        )[LoginViewModel::class.java]
        profilViewModel = ViewModelProvider(this)[ProfilViewModel::class.java]
        bookingViewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        bookingViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        postBooking()
        getProfile()
        btnBack()

        binding.edtIdDates.setOnClickListener {
            setCalender()
        }
    }

    private fun setCalender() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val moth = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, monthofYear, dayOfMonth ->
                val returnDate = "${monthofYear + 1} $dayOfMonth $year"
                val date = StringHelper.parseDate(
                    "MM dd yyyy",
                    "MM/dd/yy",
                    returnDate
                )
                binding.edtIdDates.setText(StringHelper.parseDate("MM/dd/yy", "MM dd yyyy", date))
                binding.edtIdDates.error = null
            }, year , moth, day
        )
        dpd.show()
    }

    private fun btnBack() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
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

    private fun postBooking() {
        binding.edtIdAngkringan.setText(intent.getStringExtra("resto"))
        binding.btnBooking.setOnClickListener {
            val resto_code = binding.edtIdAngkringan.text.toString().trim()
            val note = binding.edtIdNotes.text.toString().trim()
            val dates = binding.edtIdDates.text.toString().trim()
            when {
                resto_code.isEmpty() -> {
                    binding.edtIdAngkringan.error = "Angkringan Tidak Ada"
                }
                note.isEmpty() -> {
                    binding.edtIdNotes.error = "Harap Masukan Catatan Untuk Penjual"
                }
                dates.isEmpty() -> {
                    binding.edtIdDates.error = "Harap Masukan Tanggal"
                }
                else -> {
                    loginViewModel.getToken().observe(this) { token ->
                        if (token != null) {
                            if (dates.isNotBlank() && resto_code.isNotBlank() && dates.isNotBlank()) {
                                bookingViewModel.isLoading.observe(this) {
                                    showLoading(it)
                                }
                                bookingViewModel.booking("Bearer $token", dates, resto_code, note)
                                startActivity(Intent(this, SuccessBookingActivity::class.java))
                                finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar7.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}