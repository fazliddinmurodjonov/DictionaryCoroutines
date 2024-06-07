package com.brightfuture.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brightfuture.dictionary.databinding.ItemWordBookmarkBinding
import com.brightfuture.room.entity.Word

class WordsBookmarkAdapter : ListAdapter<Word, WordsBookmarkAdapter.ViewHolder>(MyDiffUtil()) {
    private lateinit var itemClick: OnItemClickListener
    private var wordList: MutableList<Word> = mutableListOf()

    fun interface OnItemClickListener {
        fun onClick(word: Word,position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }


    inner class ViewHolder(private var binding: ItemWordBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun onBind(word: Word, position: Int) {
            Log.d("adapter", "position: ${position+1}. ${word.name}")
            binding.ordinalNumber.text = (position + 1).toString()
            binding.tvWord.text = word.name
            binding.tvWordDefinition.text = word.definition
            binding.tvWordExample.text = word.example
            binding.imgBookmark.setOnClickListener {
                itemClick.onClick(word,position)

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordBookmarkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }


    class MyDiffUtil : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return when {
                oldItem.id == newItem.id -> {
                    false
                }

                else -> true
            }
        }
    }

    override fun submitList(list: List<Word>?) {
        wordList = list?.toMutableList() ?: mutableListOf()
        super.submitList(list)
    }

}