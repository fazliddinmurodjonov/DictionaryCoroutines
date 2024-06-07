package com.brightfuture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
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
        val wordsBookmarkArrayList: ArrayList<Word> = ArrayList(wordsBookmarkList)
        imageEmpty(wordsBookmarkArrayList.size)
        wordsBookmarkAdapter.submitList(wordsBookmarkArrayList)
        binding.rvWordsBookmark.adapter = wordsBookmarkAdapter
        wordsBookmarkAdapter.setOnItemClickListener { word, position ->
            Functions.db.wordDao().updateBookmark(word.id, 0)
            wordsBookmarkArrayList.remove(word)
            binding.rvWordsBookmark.adapter?.notifyItemRemoved(position)
            binding.rvWordsBookmark.adapter?.notifyItemRangeChanged(
                position,
                wordsBookmarkArrayList.size
            )
            imageEmpty(wordsBookmarkArrayList.size)
        }
    }

    private fun imageEmpty(count: Int) {
        binding.imgEmpty.visibility = if (count == 0) View.VISIBLE else View.GONE
        binding.rvWordsBookmark.visibility = if (count == 0) View.GONE else View.VISIBLE
    }
}