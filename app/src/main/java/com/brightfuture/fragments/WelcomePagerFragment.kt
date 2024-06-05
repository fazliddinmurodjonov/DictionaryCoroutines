package com.brightfuture.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentWelcomePagerBinding
import com.brightfuture.utils.ConnectivityManagers
import com.brightfuture.viewmodels.NetworkConnectionViewModel

private const val PAGE = "page"

class WelcomePagerFragment : Fragment(R.layout.fragment_welcome_pager) {
    private val binding: FragmentWelcomePagerBinding by viewBinding()
    private var page: Int? = null
    private lateinit var networkConnectionViewModel: NetworkConnectionViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(PAGE) + 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
        clicks()
        networkConnectionViewModel = NetworkConnectionViewModel(requireActivity().application)
        networkConnectionViewModel.observe(viewLifecycleOwner) { isConnect ->
            if (page == 4) {
                Log.d("networkConnectionViewModellll", "isConnect:$isConnect ")
                if (isConnect) {
                //    loadData()
                  //  alertDialog.dismiss()
                } else {
                 //   alertDialog.show()
                }
            }
        }
    }



    private fun createUI() {
        Log.d("WelcomePagerFragment", "page:$page ")
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
                downloadingWords()
            }
        }
    }

    private fun downloadingWords() {
        binding.tvPager.text = resources.getString(R.string.pager_4)
        binding.progressBarPager.isVisible = true
        if (!ConnectivityManagers.isNetworkAvailable) {
            binding.infoLayout.visibility = View.GONE
            binding.retryConnectionLayout.root.visibility = View.VISIBLE
        }
    }
    private fun clicks() {

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

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (page==4 && menuVisible)
        {
            Log.d("networkConnectionViewModellll", "page: ${page} menuVisible:$menuVisible ")
        }
    }
}