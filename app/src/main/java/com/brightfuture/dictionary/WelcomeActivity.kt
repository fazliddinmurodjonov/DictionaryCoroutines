package com.brightfuture.dictionary

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.brightfuture.dictionary.databinding.ActivityMainBinding
import com.brightfuture.utils.CustomizeViews

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
    }

    private fun createUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_DictionaryWithCoroutines)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustomizeViews.appearanceStatusNavigationBars(window, 0)
        CustomizeViews.statusNavigationBarsColor(window, this, R.color.red_four, R.color.white)
    }
}