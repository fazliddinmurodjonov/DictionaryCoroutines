package com.brightfuture.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.brightfuture.dictionary.R
import com.brightfuture.dictionary.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding: FragmentHomeBinding by viewBinding()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.copyLayout.imgWordFunction.setImageResource(R.drawable.copy_word)
        binding.soundLayout.imgWordFunction.setImageResource(R.drawable.sound)
        binding.saveLayout.imgWordFunction.setImageResource(R.drawable.bookmark)
        binding.shareLayout.imgWordFunction.setImageResource(R.drawable.share)

        binding.copyLayout.tvWordFunction.text = resources.getText(R.string.copy).toString().lowercase()
        binding.soundLayout.tvWordFunction.text = resources.getText(R.string.sound).toString().lowercase()
        binding.saveLayout.tvWordFunction.text = resources.getText(R.string.save).toString().lowercase()
        binding.shareLayout.tvWordFunction.text = resources.getText(R.string.share).toString().lowercase()
    }
}