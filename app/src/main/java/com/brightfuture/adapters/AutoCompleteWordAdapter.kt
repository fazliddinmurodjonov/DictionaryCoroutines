package com.brightfuture.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.brightfuture.dictionary.databinding.ItemWordAutoCompliteBinding
import com.brightfuture.models.WordSearching
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.brightfuture.dictionary.R


class AutoCompleteWordAdapter(context: Context, private var words: List<WordSearching>) :
    ArrayAdapter<WordSearching>(context, 0, words) {

    override fun getCount(): Int = words.size

    override fun getItem(position: Int): WordSearching = words[position]

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val word = words[position]

        Log.d("adaptttt", "getView: ${word.name}")
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_word_auto_complite, parent, false)
        view.findViewById<TextView>(R.id.tvWord).text = word.name
        view.findViewById<ImageView>(R.id.imgDownloadWord).isVisible = !word.isExist
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                return FilterResults().apply { values = words }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                words = results?.values as? List<WordSearching> ?: emptyList()
                notifyDataSetChanged()
            }
        }
    }

    fun setWords(newWords: List<WordSearching>) {
        words = newWords
        notifyDataSetChanged()
    }
}