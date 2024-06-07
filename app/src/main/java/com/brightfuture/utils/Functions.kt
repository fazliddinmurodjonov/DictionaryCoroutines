package com.brightfuture.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.brightfuture.dictionary.R
import com.brightfuture.models.word_response.WordResponseItem
import com.brightfuture.retrofit.ApiClient
import com.brightfuture.room.database.WordDB
import com.brightfuture.room.entity.Word
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener

object Functions {
    val essentialWords = listOf(
        "the", "to", "people", "animal", "easy", "in", "that", "have", "find", "understand",
        "it", "more", "not", "on", "with", "he", "as", "you", "do", "at",
        "this", "but", "his", "by", "from", "they", "we", "buy", "her", "she",
        "or", "bark", "will", "my", "one", "all", "swallow", "there", "keyboard", "what",
        "so", "up", "out", "if", "about", "who", "get", "which", "go", "me",
        "when", "make", "can", "like", "time", "no", "just", "him", "know", "take",
        "person", "into", "year", "kid", "good", "some", "could", "mood", "see", "other",
        "than", "then", "now", "different", "only", "come", "its", "over", "think", "also",
        "back", "after", "use", "two", "how", "book", "work", "first", "well", "way",
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

    fun insertWord(wordResponseItem: WordResponseItem) {
        val word = Word()
        word.name = wordResponseItem.word
        word.phonetic = if (wordResponseItem.phonetic != null) wordResponseItem.phonetic else ""
        for (phonetic in wordResponseItem.phonetics) {
            if (phonetic.audio != null) {
                word.audioLink = phonetic.audio
                break
            }
        }
        for (meaning in wordResponseItem.meanings) {
            var isGetDefinitionAndExample = false
            for (def in meaning.definitions) {
                if (def.definition != null && def.definition.isNotEmpty() && def.example != null) {
                    word.definition = def.definition
                    word.example = def.example

                    isGetDefinitionAndExample = true
                    break
                }
            }
            if (isGetDefinitionAndExample) {
                break
            }
        }
        if (word.definition=="" && word.example=="")
        {
            Log.d(
                "repository",
                "word : ${wordResponseItem.word}"
            )
        }
        word.searched = 0
        word.bookmark = 0
        word.seen = 0
        db.wordDao().insert(word)
    }

    fun shareWord(word: Word, context: Context) {
        val shareText = "Word: ${word.name}\nDefinition: ${word.definition}\nExample: ${word.example}\nAudioLink: ${word.audioLink}"
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                shareText
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun requestLocationPermissionAgain() {
        Dexter.withContext(App.instance).withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    SharedPreference.permissionLocationForDialog = -1
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response!!.isPermanentlyDenied) {
                        if (SharedPreference.permissionLocationForDialog == 1) {
                            appDetailsSettings()
                        }
                        SharedPreference.permissionLocationForDialog = 1
                    } else {
                        SharedPreference.permissionLocationForDialog = 0
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?,
                ) {
                    token?.continuePermissionRequest()
                }
            }).check()
    }


}