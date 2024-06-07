package com.brightfuture.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsHistoryAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHistoryBinding
import com.brightfuture.utils.Functions

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private val binding: FragmentHistoryBinding by viewBinding()
    val wordsHistoryAdapter = WordsHistoryAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    private fun createUI() {
        val wordsHistoryList = Functions.db.wordDao().getAllSeenWords(1)
        val wordsIdList = wordsHistoryList.map { it.id }
        wordsHistoryAdapter.submitList(wordsHistoryList)
        binding.rvWordsHistory.adapter = wordsHistoryAdapter
        binding.cvClearHistory.setOnClickListener {
            Functions.db.wordDao().updateSeenList(wordsIdList,0)
            binding.rvWordsHistory.adapter = null
        }
    }
}