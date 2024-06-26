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
    var audioPermission: Int
        get() = sharedPreference.getInt("audioPermission", 100)
        set(value) = sharedPreference.edit {
            it.putInt("audioPermission", value)
        }

    var upperCaseFont: Int
        get() = sharedPreference.getInt("upperCaseFont", 0)
        set(value) = sharedPreference.edit {
            it.putInt("upperCaseFont", value)
        }

}