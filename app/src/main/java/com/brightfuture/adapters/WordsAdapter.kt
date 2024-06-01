package com.brightfuture.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brightfuture.dictionary.databinding.ItemWordBinding
import com.brightfuture.room.entity.Word

class WordsAdapter() : ListAdapter<Word, WordsAdapter.ViewHolder>(MyDiffUtil()) {
    lateinit var itemClick: OnItemClickListener
    private var wordList: MutableList<Word> = mutableListOf()

    fun interface OnItemClickListener {
        fun onClick(id: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClick = listener
    }


    inner class ViewHolder(private var binding: ItemWordBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceAsColor")
        fun onBind(word: Word,position: Int) {
            binding.ordinalNumber.text = word.id.toString()
            binding.tvWordExample
            binding.root.setOnClickListener {
                removeItem(bindingAdapterPosition)
            }
        }
    }

    fun removeItem(position: Int) {
        if (position < wordList.size) {
            wordList.removeAt(position)
            submitList(wordList.toList())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position),position)
    }

    class MyDiffUtil : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(
            oldItem: Word,
            newItem: Word,
        ): Boolean {
            return oldItem.id == newItem.id
        }


        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Word,
            newItem: Word,
        ): Boolean {
            return when {
                oldItem.id != newItem.id -> {
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