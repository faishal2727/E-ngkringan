package com.example.e_ngkringan.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.e_ngkringan.adapter.MenuAdapter
import com.example.e_ngkringan.databinding.ActivityMenuBinding
import com.example.e_ngkringan.detail.DetailActivity
import com.example.e_ngkringan.model.DetailMenu

class ActivityMenu : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private lateinit var menuAdapter: MenuAdapter
    private lateinit var menuViewModel: MenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        menuViewModel = ViewModelProvider(this)[MenuViewModel::class.java]
        menuViewModel.listMenu.observe(this) {
            setRecyler(it)
        }

        menuViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val code = intent.getStringExtra("code")
        if (code != null) {
            menuViewModel.getMenu(code)
        }

        back()
        search()
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, DetailActivity::class.java))
            finish()
        }
    }

    private fun setRecyler(listMenu: List<DetailMenu>) {
        menuAdapter = MenuAdapter(listMenu)
        binding.rvMenu.apply {
            adapter = menuAdapter
            layoutManager = GridLayoutManager(this@ActivityMenu, 2)
        }
    }

    private fun search() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    menuViewModel.searchMenu(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}