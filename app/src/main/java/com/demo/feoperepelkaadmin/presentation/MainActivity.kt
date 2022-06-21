package com.demo.feoperepelkaadmin.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.feoperepelkaadmin.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatDelegate

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
