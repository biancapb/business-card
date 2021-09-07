package com.example.businesscard.ui

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.observe
import com.example.businesscard.App
import com.example.businesscard.databinding.ActivityMainBinding
import com.example.businesscard.util.Image

class MainActivity : AppCompatActivity() {

    //vantagem: poder utilizar uma forma mais simples de não dar problemas com nulos, gerenciar nullability
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }
    private val adapter by lazy { BusinessCardAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpPermissions()
        binding.rvCards.adapter = adapter
        getAllBusinessCard()
        insertListeners()
    }

    private fun setUpPermissions() {
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
        )
        ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
        )
    }

    private fun insertListeners() {
        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddBusinessCardActivity::class.java)
            startActivity(intent)
        }
        adapter.listenerShare = { card ->
            Image.share(this@MainActivity, card)
        }
    }

    private fun getAllBusinessCard() {
        mainViewModel.getAll().observe(this, { businessCards ->
            adapter.submitList(businessCards)
        })
    }
}