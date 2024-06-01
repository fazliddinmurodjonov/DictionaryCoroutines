package com.brightfuture.fragments

import android.os.Bundle
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

class BookmarkFragment : Fragment(R.layout.fragment_bookmark) {
    private val binding : FragmentBookmarkBinding by viewBinding()
    private val bookmarkWordsAdapter = WordsBookmarkAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val wordsList = ArrayList<Word>()
        for (i in 0..100) {
            wordsList.add(Word(name = "Hello",
                phonetic = "fɪˈnɛtɪk",
                audioLink = "http://example.com/audio.mp3",
                definition = "The study and classification of speech sounds",
                example = "She is studying phonetics",
                bookmark = 1,
                seen = 1
            ))
        }
        bookmarkWordsAdapter.submitList(wordsList)
        binding.rvWordsBookmark.adapter = bookmarkWordsAdapter

    }
}