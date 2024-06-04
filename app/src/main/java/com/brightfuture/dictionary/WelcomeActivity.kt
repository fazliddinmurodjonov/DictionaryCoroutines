package com.brightfuture.dictionary

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.brightfuture.adapters.WelcomePagerAdapter
import com.brightfuture.dictionary.databinding.ActivityWelcomeBinding
import com.brightfuture.utils.Functions
import com.brightfuture.utils.CustomizeViews
import com.google.android.material.tabs.TabLayoutMediator


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var welcomePagerAdapter: WelcomePagerAdapter
    var page = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
    }

    private fun createUI() {
        Functions.connectivityManager(this)
        screen()
        setAdapter()
        clicks()
    }

    private fun setAdapter() {
        welcomePagerAdapter = WelcomePagerAdapter(4, this)
        binding.welcomeViewPager.offscreenPageLimit = 1
        binding.welcomeViewPager.adapter = welcomePagerAdapter
        TabLayoutMediator(binding.welcomeTabLayout, binding.welcomeViewPager) { tab, position ->
        }.attach()
        binding.welcomeViewPager.apply {
            adapter = welcomePagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        val myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                Log.d("onPageSelected", "position: $position")
                page = position
                if (position == 3) {
                    binding.welcomeViewPager.isUserInputEnabled = false
                }
            }
        }
        binding.welcomeViewPager.registerOnPageChangeCallback(myPageChangeCallback)
    }

    private fun clicks() {
        binding.skipLayout.setOnClickListener {
            if (page != 3) {
                binding.welcomeViewPager.setCurrentItem(3, true)
            }
        }
    }

    private fun screen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            installSplashScreen()
        } else {
            setTheme(R.style.Theme_DictionaryWithCoroutines)
        }
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustomizeViews.appearanceStatusNavigationBars(window, 1)
        CustomizeViews.statusNavigationBarsColor(window, this, R.color.red_four, R.color.red_two)
    }

    override fun attachBaseContext(newBase: Context?) {
        val newOverride = Configuration(newBase?.resources?.configuration)
        newOverride.fontScale = 1.0f
        applyOverrideConfiguration(newOverride)
        super.attachBaseContext(newBase)
    }

}