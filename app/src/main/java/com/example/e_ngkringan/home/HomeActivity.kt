package com.example.e_ngkringan.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_ngkringan.detail.DetailActivity
import com.example.e_ngkringan.adapter.AngkringanAdapter
import com.example.e_ngkringan.databinding.ActivityHomeBinding
import com.example.e_ngkringan.model.AngkringanDetail
import com.example.e_ngkringan.profil.ProfilActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var angkringanAdapter: AngkringanAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setViewModel()
        search()
        showProfil()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setViewModel() {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.listAngkringan.observe(this) {
            setRecyler(it)
        }
        homeViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        homeViewModel.getAllAngkringan()
    }

    private fun setRecyler(listAngkringan: List<AngkringanDetail>) {
        angkringanAdapter =
            AngkringanAdapter(listAngkringan, object : AngkringanAdapter.OnItemCallBack {
                override fun onItemClicked(data: AngkringanDetail) {
                    startActivity(Intent(this@HomeActivity, DetailActivity::class.java).also {
                        it.putExtra("id", data.id)
                    })
                }
            })
        binding.rv.apply {
            adapter = angkringanAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
    }

    private fun showProfil() {
        binding.ivAvatar.setOnClickListener {
            startActivity(Intent(this, ProfilActivity::class.java))
        }
    }

    private fun search() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    homeViewModel.getSearch(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                homeViewModel.getSearch(newText.toString())
                return true
            }

        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}