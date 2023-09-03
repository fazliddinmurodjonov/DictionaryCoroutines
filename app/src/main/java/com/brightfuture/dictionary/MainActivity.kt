package com.brightfuture.dictionary

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.dictionary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.appBarMain.contentMain.btnNav.itemIconTintList = null
        val rippleColor = ColorDrawable(ContextCompat.getColor(this, R.color.red_four))
        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this,R.color.red_four))

        //binding.appBarMain.contentMain.btnNav.itemRippleColor =colorStateList

    }
}