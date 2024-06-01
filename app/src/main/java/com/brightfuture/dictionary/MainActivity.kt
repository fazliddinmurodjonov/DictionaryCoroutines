package com.brightfuture.dictionary

import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.brightfuture.utils.CustomValues
import com.brightfuture.utils.CustomizeViews
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
        createNavigation()
    }

    private fun createUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustomizeViews.appearanceStatusNavigationBars(window, 0)
        CustomizeViews.statusNavigationBarsColor(window, this, R.color.red_four, R.color.white)
        clickMenu()
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
            CustomValues.navigationFragmentList,
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
            CustomizeViews.showBottomSheetDialog(supportFragmentManager, "about_app")
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