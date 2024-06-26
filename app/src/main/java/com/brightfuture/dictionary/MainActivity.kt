package com.brightfuture.dictionary

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.brightfuture.dictionary.databinding.ActivityMainBinding
import com.brightfuture.utils.Functions
import com.brightfuture.utils.CustomizeViews
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
        Functions.connectivityManager(this)
        createNavigation()
    }

    private fun createUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustomizeViews.appearanceStatusNavigationBars(window, 0)
        CustomizeViews.statusNavigationBarsColor(window, this, R.color.red_four, R.color.white)
        clickMenu()
        changeFontOfNavView()

    }

    private fun changeFontOfNavView() {
        val typeface = ResourcesCompat.getFont(this, R.font.roboto_slab)
        for (i in 0 until binding.navView.menu.size()) {
            val menuItem = binding.navView.menu.getItem(i)
            val spanString = SpannableString(menuItem.title)
            spanString.setSpan(
                TypefaceSpan(typeface!!),
                0,
                spanString.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            menuItem.title = spanString
        }
    }

    private fun createNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController
        val navView: NavigationView = binding.navView
        val bottomNavigationView = binding.appBarMain.contentMain.btnNav
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        navView.itemIconTintList = null
        bottomNavigationView.itemIconTintList = null
        appBarConfiguration = AppBarConfiguration(
            Functions.navigationFragmentList,
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.appBarMain.contentMain.btnNav.setupWithNavController(navController)
        NavigationUI.setupWithNavController(
            binding.appBarMain.toolbar,
            navController,
            appBarConfiguration
        )
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.appBarMain.toolbar.setNavigationIcon(R.drawable.menu_hamburger)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun clickMenu() {
        val shareItem = binding.navView.menu.findItem(R.id.nav_share)
        val aboutItem = binding.navView.menu.findItem(R.id.nav_info)
        shareItem.setOnMenuItemClickListener {
            CustomizeViews.shareApp(this)
            closeDrawer()
            true
        }
        aboutItem.setOnMenuItemClickListener {
            CustomizeViews.showDialog(supportFragmentManager, "about_app")
            closeDrawer()
            true
        }

    }

    private fun closeDrawer() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
        super.attachBaseContext(newBase)
    }


}