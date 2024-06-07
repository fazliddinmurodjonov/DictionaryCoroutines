package com.brightfuture.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val NAME = "Dictionary"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreference: SharedPreferences
    fun init(context: Context) {
        sharedPreference = context.getSharedPreferences(NAME, MODE)
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isWordsDownloaded: Boolean
        get() = sharedPreference.getBoolean("isWordsDownloaded", false)
        set(value) = sharedPreference.edit {
            it.putBoolean("isWordsDownloaded", value)
        }
    var isWordsDownloaded: Boolean
        get() = sharedPreference.getBoolean("isWordsDownloaded", false)
        set(value) = sharedPreference.edit {
            it.putBoolean("isWordsDownloaded", value)
        }

}