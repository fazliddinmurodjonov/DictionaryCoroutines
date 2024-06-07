package com.brightfuture.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHomeBinding
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.Functions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    var word = Word()
    private lateinit var textToSpeech: TextToSpeech

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    private fun createUI() {
        clicks()
        with(binding)
        {
            copyLayout.imgWordFunction.setImageResource(R.drawable.copy_word)
            soundLayout.imgWordFunction.setImageResource(R.drawable.sound)
            bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_word)
            shareLayout.imgWordFunction.setImageResource(R.drawable.share)
            copyLayout.tvWordFunction.text = resources.getText(R.string.copy).toString().lowercase()
            soundLayout.tvWordFunction.text =
                resources.getText(R.string.sound).toString().lowercase()
            bookmarkLayout.tvWordFunction.text =
                resources.getText(R.string.save).toString().lowercase()
            shareLayout.tvWordFunction.text =
                resources.getText(R.string.share).toString().lowercase()
        }
        binding.wordLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.wordLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val heightInPx: Int = binding.wordLayout.height
                //val heightInDp = pixelToDp(heightInPx)
                val layoutParams: ViewGroup.LayoutParams? = binding.rvAllWords.layoutParams
                layoutParams!!.height = heightInPx
                binding.rvAllWords.layoutParams = layoutParams
            }
        })
        wordsAdapter()
        randomWord()
        lifecycleScope.launch {
            val wordCount = withContext(Dispatchers.IO) {
                Functions.db.wordDao().getCountOfWords()
            }
            binding.tvFoundWords.text = getString(R.string.found_words, wordCount)
        }
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.UK
            }
        }
    }

    private fun randomWord() {
        val randomWord = Functions.db.wordDao().getRandomWord()
        setWordToViews(randomWord)
    }

    private fun setWordToViews(w: Word) {
        word = w
        binding.tvWord.text = w.name
        binding.wordDefinition.text = w.definition
        binding.wordExample.text = w.example
        Functions.db.wordDao().updateSeen(w.id, 1)
        if (word.bookmark == 1) {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_fill_word)
        } else {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_word)
        }
    }

    private fun wordsAdapter() {
        val wordsAdapter = WordsAdapter()
        val wordsList = Functions.db.wordDao().getAllWords()
        wordsAdapter.submitList(wordsList)
        binding.rvAllWords.adapter = wordsAdapter
        binding.autoCompleteText.isSingleLine = true
    }

    private fun clicks() {
        binding.tvWordOfTheDay.setOnClickListener {
            wordsDayAndAll(false)
        }
        binding.tvAllWords.setOnClickListener {
            wordsDayAndAll(true)
        }
        binding.imgRandomWord.setOnClickListener {
            randomWord()
        }
        binding.cvCopyOfSearch.setOnClickListener {
            pasteTextFromClipboard()
        }
        binding.copyLayout.cvWordFunction.setOnClickListener {
            copyTextFromClipboard(word.name)
        }
        binding.soundLayout.cvWordFunction.setOnClickListener {
            listenAudio()
        }
        binding.bookmarkLayout.cvWordFunction.setOnClickListener {
            bookmarkWord()
        }
        binding.shareLayout.cvWordFunction.setOnClickListener {
            Functions.shareWord(word, requireContext())
        }
    }

    private fun listenAudio() {
//        if (networkConnected) {
//            val player = MediaPlayer.create(requireContext(),
//                Uri.parse("https:${currentWord?.audio_link}"))
//            if (player != null)
//                player.start()
//            else {
//                tts.speak(currentWord?.word, TextToSpeech.QUEUE_FLUSH, null, "null")
//            }
//        } else {
//            tts.speak(currentWord?.word, TextToSpeech.QUEUE_FLUSH, null, "null")
//        }


        val player = MediaPlayer.create(
            requireContext(),
            Uri.parse(word.audioLink)
        )
        if (player != null)
            player.start()
        else {
            textToSpeech.speak(word.name, TextToSpeech.QUEUE_FLUSH, null, "null")
        }

        //  textToSpeech.speak(word.name, TextToSpeech.QUEUE_FLUSH, null, "null")

    }

    private fun bookmarkWord() {
        if (word.bookmark == 1) {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_word)
            word.bookmark = 0
        } else {
            binding.bookmarkLayout.imgWordFunction.setImageResource(R.drawable.bookmark_fill_word)
            word.bookmark = 1
        }
        Functions.db.wordDao().updateBookmark(word.id, word.bookmark)
    }

    private fun pasteTextFromClipboard() {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        val item = clipData?.getItemAt(0)
        binding.autoCompleteText.setText(item?.text?.toString())
    }

    private fun copyTextFromClipboard(text: String) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Dictionary", text)
        clipboardManager.setPrimaryClip(clipData)
        // Optionally, show a toast to inform the user
        Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun wordsDayAndAll(isAllWords: Boolean) {
        binding.wordOfTheDayLayout.visibility = if (isAllWords) View.GONE else View.VISIBLE
        binding.rvAllWords.visibility = if (isAllWords) View.VISIBLE else View.GONE
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        val colorWhite70 = ContextCompat.getColor(requireContext(), R.color.white_70)
        val colorWordOfTheDay = if (isAllWords) colorWhite70 else colorWhite
        val colorAllWords = if (isAllWords) colorWhite else colorWhite70
        binding.tvWordOfTheDay.setTextColor(colorWordOfTheDay)
        binding.tvAllWords.setTextColor(colorAllWords)
    }

//    @SuppressLint("SetTextI18n")
//    private fun loadData() {
//      //    listWord = database.wordDao().getAllWords()
//
//       // binding.foundWordsTv.text = "found ${listWord!!.size} words offline"
//
//        //load item
//    //    val lastWord = sPref.getString("last", "")
////        currentWord = if (lastWord.equals("")) {
////            listWord?.get(0)
////        } else {
////            database.wordDao().getWordByName(lastWord!!)
////        }
//       // loadWord(currentWord!!)
//
//      //  val itemList: java.util.ArrayList<MyItem> = arrayListOf()
////        listWord!!.forEach {
////            itemList.add(MyItem(it.word))
////        }
//       val  adapter = AutoCompleteAdapter(requireContext(), itemList)
//        binding.autoCompleteText.setAdapter(adapter)
//        binding.autoCompleteText.onItemClickListener = object : AdapterView.OnItemClickListener {
//            @SuppressLint("SetTextI18n")
//            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val myItem = adapter.getItem(p2) as MyItem
//                if (myItem.isExist) {
//                    val word = database.wordDao().getWordByName(myItem.name)
//                    loadWord(word)
//                    binding.autoCompleteText.text.clear()
//                    switchOneAndAll(false)
//                    saveSearchItem()
//                } else {
//                    fetchAndSaveWord(myItem.name)
//                }
//                hideKeyboard()
//            }
//
//        }
//
//
//        allItemAdapter = WordAdapter(listWord!!, object : WordAdapter.OnItemClickListener {
//            override fun onItemClick(wordEntity: WordEntity) {
//                loadWord(wordEntity)
//                switchOneAndAll(false)
//            }
//
//        })
//
//        binding.rv.adapter = allItemAdapter
//        binding.rv.addItemDecoration(
//            DividerItemDecoration(binding.rv.context,
//            DividerItemDecoration.VERTICAL)
//        )
//    }
}