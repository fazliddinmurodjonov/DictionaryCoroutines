package com.brightfuture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentWelcomePagerBinding

private const val PAGE = "page"

class WelcomePagerFragment : Fragment(R.layout.fragment_welcome_pager) {
    private val binding: FragmentWelcomePagerBinding by viewBinding()
    private var page: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(PAGE) + 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (page) {
            1 -> {
                binding.tvPager.text = resources.getString(R.string.pager_1)
            }

            2 -> {
                binding.tvPager.text = resources.getString(R.string.pager_2)
            }

            3 -> {
                binding.tvPager.text = resources.getString(R.string.pager_3)
            }

            4 -> {
                binding.tvPager.text = resources.getString(R.string.pager_4)
                binding.progressBarPager.isVisible = true
            }
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(page: Int) =
            WelcomePagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(PAGE, page)
                }
            }
    }
}