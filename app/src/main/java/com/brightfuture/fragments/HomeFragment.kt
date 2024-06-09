package com.brightfuture.fragments

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.AutoCompleteWordAdapter
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHomeBinding
import com.brightfuture.models.WordSearching
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.ConnectivityManagers
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Functions.copyTextFromClipboard
import com.brightfuture.utils.Resource
import com.brightfuture.utils.SharedPreference
import com.brightfuture.utils.Status
import com.brightfuture.viewmodels.DictionaryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayList
import java.util.Locale


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    var word = Word()
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var dictionaryViewModel: DictionaryViewModel
    private lateinit var permissionRecordAudio: Observer<Int>
    private lateinit var wordObserver: Observer<Resource<Long>>
    private var speechRecognizer: SpeechRecognizer? = null
    private var speechRecognizerIntent: Intent? = null
    private lateinit var autoCompleteWordAdapter: AutoCompleteWordAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createUI()
    }

    private fun createUI() {
        SharedPreference.init(requireContext())
        clicks()
        viewModelAndObservers()
        with(binding) {
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
        setFoundWordsCount()
        textToSpeech = TextToSpeech(requireContext()) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.UK
            }
        }
        autoCompleteTextView()
    }

    private fun setFoundWordsCount() {
        lifecycleScope.launch {
            val wordCount = withContext(Dispatchers.IO) {
                Functions.db.wordDao().getCountOfWords()
            }
            binding.tvFoundWords.text = getString(R.string.found_words, wordCount)
        }
    }

    private fun autoCompleteTextView() {
        autoCompleteWordAdapter = AutoCompleteWordAdapter(requireContext(), emptyList())
        binding.autoCompleteText.setAdapter(autoCompleteWordAdapter)
        lifecycleScope.launch {
            dictionaryViewModel.suggestions.collectLatest { wordList ->
                autoCompleteWordAdapter.setWords(wordList)
            }
        }
        binding.autoCompleteText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {
                dictionaryViewModel.searchingWords(s.toString())
            }
        })

        binding.autoCompleteText.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selectWord = autoCompleteWordAdapter.getItem(position)
                if (selectWord.id != 0L) {
                    Functions.db.wordDao().updateSearched(selectWord.id, 1)
                    setWordToViews(Functions.db.wordDao().getWordById(selectWord.id))
                } else {
                    if (ConnectivityManagers.isNetworkAvailable) {
                        dictionaryViewModel.getWord(selectWord.name)
                            .observe(viewLifecycleOwner, wordObserver)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            resources.getText(R.string.no_internet_connection),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                binding.autoCompleteText.setText("")
                hideKeyboard()
            }

    }

    private fun viewModelAndObservers() {
        dictionaryViewModel = ViewModelProvider(this)[DictionaryViewModel::class.java]
        permissionRecordAudio = Observer {
            when (it) {
                1 -> {
                    SharedPreference.audioPermission = it
                    setUpSpeech()
                }

                0 -> {
                    SharedPreference.audioPermission = it
                }

                -1 -> {
                    SharedPreference.audioPermission = it
                    Functions.appDetailsSettings()
                }
            }
        }
        wordObserver = Observer {
            when (it.status) {
                Status.LOADING -> {

                }

                Status.SUCCESS -> {
                    val word = Functions.db.wordDao().getWordById(it.data!!)
                    Functions.db.wordDao().updateSearched(it.data, 1)
                    setWordToViews(word)
                    setFoundWordsCount()
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
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
        val wordsAdapter = WordsAdapter(false)
        val wordsList = Functions.db.wordDao().getAllWords()
        wordsAdapter.submitList(wordsList)
        binding.rvAllWords.adapter = wordsAdapter
        binding.autoCompleteText.isSingleLine = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clicks() {
        binding.tvWordOfTheDay.setOnClickListener {
            wordsDayAndAll(false)
        }
        binding.tvAllWords.setOnClickListener {
            wordsDayAndAll(true)
            hideKeyboard()
        }
        binding.imgRandomWord.setOnClickListener {
            randomWord()
        }
        binding.cvCopyOfSearch.setOnClickListener {
            pasteTextFromClipboard()
        }
        binding.cvMicrophoneOfSearch.setOnClickListener {
            if (SharedPreference.audioPermission == 1) {
                setUpSpeech()
                binding.cvMicrophoneOfSearch.setOnTouchListener { view, motionEvent ->
                    if (motionEvent.action == MotionEvent.ACTION_UP) {
                        speechRecognizer!!.stopListening()
                    }
                    if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                        binding.imageMicrophoneOfSearch.setImageResource(R.drawable.microphone)
                        speechRecognizer!!.startListening(speechRecognizerIntent!!)
                    }
                    false
                }
            } else {
                dictionaryViewModel.permissionOfRecordAudio(requireContext())
                    .observe(viewLifecycleOwner, permissionRecordAudio)
            }
        }

        binding.copyLayout.cvWordFunction.setOnClickListener {
            copyTextFromClipboard(word.name,requireContext())
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


    private fun setUpSpeech() {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()
        )
        speechRecognizer!!.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {
                binding.autoCompleteText.hint = "Listening..."
            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(bundle: Bundle?) {
                binding.imageMicrophoneOfSearch.setImageResource(R.drawable.microphone)
                val data: ArrayList<String> =
                    bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION) as ArrayList<String>
                binding.autoCompleteText.setText(data[0])
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }

        })
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

    override fun onResume() {
        super.onResume()
        if (Functions.isRecordAudioPermissionGranted(requireContext())) {
            SharedPreference.audioPermission = 1
        } else {
            if (SharedPreference.audioPermission != -1) {
                SharedPreference.audioPermission = 0
            }
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun searchingWords() {
//
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

    fun hideKeyboard() {
        requireActivity().currentFocus?.let { view ->
            val imm =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}