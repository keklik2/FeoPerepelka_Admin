package com.demo.feoperepelkaadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.demo.architecture.helpers.setVisibility
import com.demo.feoperepelkaadmin.databinding.ActivityMainBinding
import com.demo.feoperepelkaadmin.server.Server
import kotlin.Exception
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import com.parse.ParseObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCategory.setOnClickListener {

        }

        binding.buttonProduct.setOnClickListener {

        }

        binding.buttonOrder.setOnClickListener {

        }
    }
}
