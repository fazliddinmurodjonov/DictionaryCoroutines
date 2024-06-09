package com.brightfuture.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.content.ContextCompat
import com.brightfuture.dictionary.R
import com.brightfuture.models.word_response.WordResponseItem
import com.brightfuture.retrofit.ApiClient
import com.brightfuture.room.database.WordDB
import com.brightfuture.room.entity.Word
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
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

    fun insertWord(wordResponseItem: WordResponseItem):Long {
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
        if (word.definition == "" && word.example == "") {
            Log.d(
                "repository",
                "word : ${wordResponseItem.word}"
            )
        }
        word.searched = 0
        word.bookmark = 0
        word.seen = 0
        db.wordDao().insert(word)
        return db.wordDao().getIdByName(word.name).id
    }

    fun shareWord(word: Word, context: Context) {
        val shareText =
            "Word: ${word.name}\nDefinition: ${word.definition}\nExample: ${word.example}\nAudioLink: ${word.audioLink}"
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

    fun audioPermission() {
        SharedPreference.init(App.instance)
        when (SharedPreference.audioPermission) {
            -1 -> requestRecordAudioPermission()
            0 -> requestRecordAudioPermission()
            1 -> requestRecordAudioPermission()
        }
    }

    private fun requestRecordAudioPermission() {
        Dexter.withContext(App.instance).withPermission(Manifest.permission.RECORD_AUDIO)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    SharedPreference.audioPermission = 1
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    if (response!!.isPermanentlyDenied) {
                        SharedPreference.audioPermission = -1
                        appDetailsSettings()

                    } else {
                        SharedPreference.audioPermission = 0
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

    fun isRecordAudioPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun appDetailsSettings() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", App.instance.packageName, null)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        App.instance.startActivity(intent)
    }

}