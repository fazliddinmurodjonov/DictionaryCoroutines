package com.brightfuture.fragments

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHomeBinding
import com.brightfuture.room.entity.Word
import com.brightfuture.utils.CustomizeViews.pixelToDp


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()

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
            saveLayout.imgWordFunction.setImageResource(R.drawable.bookmark)
            shareLayout.imgWordFunction.setImageResource(R.drawable.share)
            copyLayout.tvWordFunction.text = resources.getText(R.string.copy).toString().lowercase()
            soundLayout.tvWordFunction.text =
                resources.getText(R.string.sound).toString().lowercase()
            saveLayout.tvWordFunction.text = resources.getText(R.string.save).toString().lowercase()
            shareLayout.tvWordFunction.text =
                resources.getText(R.string.share).toString().lowercase()
        }
        binding.wordLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.wordLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val heightInPx: Int = binding.wordLayout.height
                val heightInDp = pixelToDp(heightInPx)
                val layoutParams: ViewGroup.LayoutParams? = binding.rvAllWords.layoutParams
                layoutParams!!.height = heightInPx
                binding.rvAllWords.layoutParams = layoutParams
            }
        })
        val wordsAdapter = WordsAdapter()
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
    }

    private fun wordsDayAndAll(isAllWords: Boolean) {
        binding.wordOfTheDayLayout.visibility = if (isAllWords) View.GONE else View.VISIBLE
        binding.rvAllWords.visibility = if (isAllWords) View.VISIBLE else View.GONE
        val colorWhite = ContextCompat.getColor(requireContext(), R.color.white)
        val colorWhite70 = ContextCompat.getColor(requireContext(), R.color.white_70)
        val colorWordOfTheDay = if (isAllWords)  colorWhite70 else  colorWhite
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
//                if (myItem.isExsist) {
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