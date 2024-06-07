package com.brightfuture.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brightfuture.dictionary.databinding.ItemWordHistoryBinding
import com.brightfuture.models.WordHistory

class WordsHistoryAdapter() : ListAdapter<WordHistory, WordsHistoryAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener
  //  private var wordList: MutableList<Word> = mutableListOf()

    fun interface OnItemClickListener {
        fun onClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }


    inner class ViewHolder(private var binding: ItemWordHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun onBind(word: WordHistory, position: Int) {
            binding.tvWord.text = word.name
//            binding.root.setOnClickListener {
//                removeItem(bindingAdapterPosition)
//            }
        }
    }

//    fun removeItem(position: Int) {
//        if (position < wordList.size) {
//            wordList.removeAt(position)
//            submitList(wordList.toList())
//        }
//    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<WordHistory>() {
        override fun areItemsTheSame(
            oldItem: WordHistory,
            newItem: WordHistory,
        ): Boolean {
            return oldItem.id == newItem.id
        }


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: WordHistory,
            newItem: WordHistory,
        ): Boolean {
            return when {
                oldItem.id != newItem.id -> {
                    false
                }

                else -> true
            }
        }

    }

//    override fun submitList(list: List<WordHistory>?) {
//        wordList = list?.toMutableList() ?: mutableListOf()
//        super.submitList(list)
//    }
}