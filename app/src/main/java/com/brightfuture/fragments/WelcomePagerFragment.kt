package com.brightfuture.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.dictionary.MainActivity
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentWelcomePagerBinding
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
import com.brightfuture.utils.SharedPreference
import com.brightfuture.utils.Status
import com.brightfuture.viewmodels.DictionaryViewModel
import com.brightfuture.viewmodels.NetworkConnectionViewModel

private const val PAGE = "page"

class WelcomePagerFragment : Fragment(R.layout.fragment_welcome_pager) {
    private val binding: FragmentWelcomePagerBinding by viewBinding()
    private var page: Int? = null
    private lateinit var networkConnectionViewModel: NetworkConnectionViewModel
    private var isFourPageVisible = false
    private var networkConnection = false
    private lateinit var wordsObserver: Observer<Resource<Pair<Boolean, Boolean>>>
    private lateinit var dictionaryViewModel: DictionaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            page = it.getInt(PAGE) + 1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreference.init(requireContext())
        createUI()
        viewModelAndObservers()
        networkConnectionViewModel.observe(viewLifecycleOwner) { isConnect ->
            networkConnection = isConnect
            if (isFourPageVisible) {
                Log.d("viewModelConnection", "onViewCreated: $isConnect")
                if (isConnect) {
                    downloadingWords()
                } else {
                    downloadingWords()
                }
            }
        }
    }

    private fun viewModelAndObservers() {
        dictionaryViewModel = ViewModelProvider(requireActivity())[DictionaryViewModel::class.java]
        networkConnectionViewModel = NetworkConnectionViewModel(requireActivity().application)
        var count = 0
        wordsObserver = Observer {
            when (it.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    ++count
                    if (it.data!!.first && it.data.second) {
                        binding.progressBarPager.progress = count
                        startActivity()
                    } else {
                        binding.progressBarPager.progress = count
                    }
                }

                Status.ERROR -> {
                    if (Functions.db.wordDao().getCountOfWords() != 0) {
                        startActivity()
                    }
                    Log.d("wordsObserver", "viewModelAndObservers: ${it.message} ")
                }
            }
        }
    }

    private fun downloadingWords() {
        if (networkConnection) {
            binding.tvPager.text = resources.getString(R.string.pager_4)
            binding.progressBarPager.progress = 0
            binding.progressBarPager.isVisible = true
            binding.infoLayout.visibility = View.VISIBLE
            binding.retryConnectionLayout.root.visibility = View.GONE
            dictionaryViewModel.getWords().observe(viewLifecycleOwner, wordsObserver)
        } else {
            binding.infoLayout.visibility = View.GONE
            binding.retryConnectionLayout.root.visibility = View.VISIBLE
            if (Functions.db.wordDao().getCountOfWords() != 0) {
                startActivity()
            }
        }
    }


    private fun startActivity() {
        SharedPreference.isWordsDownloaded = true
        requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
        requireActivity().finish()
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
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(page: Int) = WelcomePagerFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE, page)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        if (Functions.db.wordDao().getCountOfWords() != 0) {
            SharedPreference.isWordsDownloaded = true
            startActivity()
        }
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        if (page == 4 && menuVisible) {
            isFourPageVisible = true
            downloadingWords()
        }
    }

}