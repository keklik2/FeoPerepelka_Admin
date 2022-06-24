package com.demo.feoperepelkaadmin.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.demo.feoperepelkaadmin.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatDelegate
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.demo.feoperepelkaadmin.R
import com.demo.feoperepelkaadmin.server.Login
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavBar: BottomNavigationBar

    @Inject
    lateinit var navHolder: NavigatorHolder
    private val navigator = AppNavigator(this, R.id.main_fragment_container)

    @Inject
    lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNavBar = findViewById<View>(R.id.nav_menu) as BottomNavigationBar

//        if (!Login.isLogged()) router.replaceScreen(Screens.Login())

        initViews()
        bottomNavBar.selectTab(1)
    }

    private fun initViews() {
        bottomNavBar
            .addItem(BottomNavigationItem(R.drawable.ic_list, R.string.menu_categories))
            .addItem(BottomNavigationItem(R.drawable.ic_menu, R.string.menu_notes))
            .addItem(BottomNavigationItem(R.drawable.ic_notification, R.string.menu_orders))
            .initialise()
        bottomNavBar.setTabSelectedListener(object : BottomNavigationBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int) {
                when (position) {
                    0 -> router.replaceScreen(Screens.CategoriesList())
                    1 -> router.replaceScreen(Screens.NotesList())
                    2 -> router.replaceScreen(Screens.OrdersList())
                }
                bottomNavBar.selectTab(position, false)
            }

            override fun onTabUnselected(position: Int) {}

            override fun onTabReselected(position: Int) {
                onTabSelected(position)
            }
        })
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
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
