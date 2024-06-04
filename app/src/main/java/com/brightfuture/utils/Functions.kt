package com.brightfuture.utils

import android.content.Context
import android.net.ConnectivityManager
import com.brightfuture.dictionary.R
import com.brightfuture.models.word_response.WordResponseItem
import com.brightfuture.retrofit.ApiClient
import com.brightfuture.room.database.WordDB
import com.brightfuture.room.entity.Word

object Functions {
    val essentialWords = listOf(
        "the", "be", "to", "of", "and", "a", "in", "that", "have", "I",
        "it", "for", "not", "on", "with", "he", "as", "you", "do", "at",
        "this", "but", "his", "by", "from", "they", "we", "say", "her", "she",
        "or", "an", "will", "my", "one", "all", "would", "there", "their", "what",
        "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
        "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
        "person", "into", "year", "your", "good", "some", "could", "them", "see", "other",
        "than", "then", "now", "look", "only", "come", "its", "over", "think", "also",
        "back", "after", "use", "two", "how", "our", "work", "first", "well", "way",
        "even", "new", "want", "because", "any", "these", "give", "day", "most", "us"
    )
    val db = WordDB.getInstance(App.instance)
    var service = ApiClient.service
    val navigationFragmentList =
        setOf(R.id.nav_home, R.id.nav_search, R.id.nav_bookmark, R.id.nav_history)

    fun connectivityManager(context: Context) {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(
            ConnectivityManagers.networkRequest,
            ConnectivityManagers.networkCallback
        )
    }

    fun insertWord(wordResponse: WordResponseItem) {
        val word = Word()
        word.name = wordResponse.word
        word.phonetic = if (wordResponse.phonetic.isNotEmpty()) wordResponse.phonetic else ""
        for (phonetic in wordResponse.phonetics) {
            if (phonetic.audio.isNotBlank()) {
                word.audioLink = phonetic.audio
                break
            }
        }
        for (meaning in wordResponse.meanings) {
            for (definition in meaning.definitions) {
                if (definition.definition !=null)
            }
        }

        word.searched  = 0
        word.bookmark  = 0
        word.seen  = 0


    }


}