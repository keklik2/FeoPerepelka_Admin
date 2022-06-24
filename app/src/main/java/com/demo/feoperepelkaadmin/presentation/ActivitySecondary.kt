package com.demo.feoperepelkaadmin.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.databinding.ActivitySecondaryBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class ActivitySecondary: AppCompatActivity() {

    private lateinit var binding: ActivitySecondaryBinding

    @Inject
    lateinit var navHolder: NavigatorHolder
    private val navigator = AppNavigator(this, R.id.main_fragment_container)

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivitySecondaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navHolder.setNavigator(navigator)
    }


    override fun onPause() {
        navHolder.removeNavigator()
        super.onPause()
    }

    companion object {
        fun newLoginIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

        fun newNoteDetailIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }

        fun newOrderDetailIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
