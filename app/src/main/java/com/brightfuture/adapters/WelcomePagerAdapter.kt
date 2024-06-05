package com.brightfuture.adapters

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brightfuture.fragments.WelcomePagerFragment

//class WelcomePagerAdapter(
//    private var pages: Int,
//    fragmentActivity: FragmentActivity,lifecycle: Lifecycle
//) : FragmentStateAdapter(fragmentActivity,lifecycle) {
//    override fun getItemCount(): Int {
//        return pages
//    }
//
//    override fun createFragment(position: Int): Fragment {
//        return WelcomePagerFragment.newInstance(position)
//    }
//}


class WelcomePagerAdapter(private var pages: Int, fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    private val fragmentCache = SparseArray<WelcomePagerFragment>()

    override fun getItemCount(): Int {
        return pages
    }

    override fun createFragment(position: Int): Fragment {
        // Lazy load the fragment when requested
        return fragmentCache.get(position) ?: WelcomePagerFragment.newInstance(position).also {
            fragmentCache.put(position, it)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }
}