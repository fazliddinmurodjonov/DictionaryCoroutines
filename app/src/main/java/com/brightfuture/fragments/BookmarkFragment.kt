package com.brightfuture.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.adapters.WordsBookmarkAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentBookmarkBinding
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.Functions

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding: FragmentBookmarkBinding by viewBinding()
    private val wordsBookmarkAdapter = WordsBookmarkAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    private fun createUI() {
        val wordsBookmarkList = Functions.db.wordDao().getAllBookmarkWords(1)
        wordsBookmarkAdapter.submitList(wordsBookmarkList)
        binding.rvWordsBookmark.adapter = wordsBookmarkAdapter
        wordsBookmarkAdapter.setOnItemClickListener {
            Functions.db.wordDao().updateBookmark(it, 0)
        }
    }
}