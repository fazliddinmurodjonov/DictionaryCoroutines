package com.brightfuture.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentSearchBinding
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.Functions

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    private fun createUI() {
        with(binding)
        {
            fontLayout.imgWordFunction.setImageResource(R.drawable.font_size)
            copyLayout.imgWordFunction.setImageResource(R.drawable.copy_word_search)
            bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_search)
            shareLayout.imgWordFunction.setImageResource(R.drawable.share_search)
            fontLayout.tvWordFunction.text =
                resources.getText(R.string.sound).toString().lowercase()
            copyLayout.tvWordFunction.text = resources.getText(R.string.copy).toString().lowercase()
            bookmarkLayout.tvWordFunction.text =
                resources.getText(R.string.save).toString().lowercase()
            shareLayout.tvWordFunction.text =
                resources.getText(R.string.share).toString().lowercase()
        }

        val wordsAdapter = WordsAdapter()
        val wordsSearchedList = Functions.db.wordDao().getAllSearchedWords(1)
        wordsAdapter.submitList(wordsSearchedList)
        binding.rvAllWords.adapter = wordsAdapter

    }


}