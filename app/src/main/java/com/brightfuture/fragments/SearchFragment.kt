package com.brightfuture.fragments


import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentSearchBinding
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.ConnectivityManagers
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Functions.copyTextFromClipboard
import java.util.Locale
import kotlin.math.sign

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding: FragmentSearchBinding by viewBinding()
    private val wordsAdapter = WordsAdapter(true)
    private lateinit var textToSpeech: TextToSpeech
    var word = Word()
    var upperCasFont = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
        clicks()
    }

    private fun clicks() {
        wordsAdapter.setOnItemClickListener {
            setWordToViews(it)
        }
        binding.imgAudio.setOnClickListener {
            listenAudio()
        }
        binding.fontLayout.cvWordFunction.setOnClickListener {

        }
        binding.copyLayout.cvWordFunction.setOnClickListener {
            copyTextFromClipboard(word.name, requireContext())
        }
        binding.bookmarkLayout.cvWordFunction.setOnClickListener {
            bookmarkWord()
        }
        binding.shareLayout.cvWordFunction.setOnClickListener {
            Functions.shareWord(word, requireContext())
        }
    }

    private fun bookmarkWord() {
        if (word.bookmark == 1) {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_search)
            word.bookmark = 0
        } else {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_fill_search)
            word.bookmark = 1
        }
        Functions.db.wordDao().updateBookmark(word.id, word.bookmark)
    }

    private fun setWordToViews(id:Long) {
        word = Functions.db.wordDao().getWordById(id)
        binding.tvWord.text = word.name
        Functions.db.wordDao().updateSeen(word.id, 1)
        if (word.bookmark == 1) {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_fill_search)
        } else {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_search)
        }
    }

    private fun createUI() {
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.UK
            }
        }
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

        val wordsSearchedList = Functions.db.wordDao().getAllSearchedWords(1)
        wordsAdapter.submitList(wordsSearchedList)
        binding.rvWordsSearched.adapter = wordsAdapter
        if (wordsSearchedList.isNotEmpty())
        {
            word= wordsSearchedList[0]
            setWordToViews(word.id)
        }
        imageEmpty(wordsSearchedList.size)
    }

    private fun listenAudio() {
        if (ConnectivityManagers.isNetworkAvailable) {
            val player = MediaPlayer.create(
                requireContext(), Uri.parse(word.audioLink)
            )
            if (player != null) player.start()
            else {
                textToSpeech.speak(word.name, TextToSpeech.QUEUE_FLUSH, null, "null")
            }
        } else {
            textToSpeech.speak(word.name, TextToSpeech.QUEUE_FLUSH, null, "null")
        }
    }


    private fun imageEmpty(count: Int) {
        binding.imgEmpty.visibility = if (count == 0) View.VISIBLE else View.GONE
        binding.searchLayout.visibility = if (count == 0) View.GONE else View.VISIBLE
    }

}