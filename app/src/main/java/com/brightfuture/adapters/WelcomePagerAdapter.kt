package com.brightfuture.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brightfuture.fragments.WelcomePagerFragment

class WelcomePagerAdapter(private var pages: Int, fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return pages
    }

    override fun createFragment(position: Int): Fragment {
        return WelcomePagerFragment.newInstance(position)
    }


}