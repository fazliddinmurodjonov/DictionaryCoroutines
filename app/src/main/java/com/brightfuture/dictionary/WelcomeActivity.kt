package com.brightfuture.dictionary

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.brightfuture.adapters.WelcomePagerAdapter
import com.brightfuture.dictionary.databinding.ActivityMainBinding
import com.brightfuture.dictionary.databinding.ActivityWelcomeBinding
import com.brightfuture.utils.CustomizeViews
import com.google.android.material.tabs.TabLayoutMediator

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createUI()
    }

    private fun createUI() {
        screen()
        val welcomePagerAdapter = WelcomePagerAdapter(4, this)
        binding.welcomeViewPager.adapter = welcomePagerAdapter
        TabLayoutMediator(binding.welcomeTabLayout, binding.welcomeViewPager) { tab, position ->
        }.attach()
        binding.welcomeViewPager.apply {
            adapter = welcomePagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            (getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        binding.welcomeViewPager.offscreenPageLimit = 1
        val myPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
            //    binding.calendarMonthTv.text = Functions.getMonthName(position)
            //    currentPosition = position
            //    Toast.makeText(this@WelcomeActivity, position.toString(), Toast.LENGTH_SHORT).show()
            }
        }
      //  binding.welcomeViewPager.isUserInputEnabled = false

        binding.welcomeViewPager.registerOnPageChangeCallback(myPageChangeCallback)
//        val size = binding.welcomeTabLayout.tabCount
//        for (i in 0 until size) {
//            val item =
//                Indicat.inflate(
//                    LayoutInflater.from(requireActivity()),
//                    null,
//                    false
//                )
//            val tabAt = binding.alphabetTL.getTabAt(i)
//            tabAt?.customView = item.root
//            item.letter.text = alphabet[i]
//        }
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