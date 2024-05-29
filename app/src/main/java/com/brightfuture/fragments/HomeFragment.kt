package com.brightfuture.fragments

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.adapters.WordsAdapter
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHomeBinding
import com.brightfuture.room.entity.Word


class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    val wordsAdapter = WordsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val screenHeight = displayMetrics.heightPixels
//        val windowMetrics = requireActivity().windowManager.currentWindowMetrics
//        val bounds = windowMetrics.bounds
//        val screenHeight = bounds.height()

        // Calculate desired height (e.g., 50% of screen height)

        // Calculate desired height (e.g., 50% of screen height)
        val desiredHeight = (screenHeight * 0.75).toInt()

        // Set height to RecyclerView

        // Set height to RecyclerView
        val params: ViewGroup.LayoutParams = binding.rvAllWords.layoutParams
        params.height = desiredHeight
        binding.rvAllWords.layoutParams = params
        createUI()
    }

    private fun createUI() {
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
        val wordsList = ArrayList<Word>()
        for (i in 0..100) {
            wordsList.add(Word(i, "$i"))
        }
        wordsAdapter.submitList(wordsList)
        binding.rvAllWords.adapter = wordsAdapter

    }
}