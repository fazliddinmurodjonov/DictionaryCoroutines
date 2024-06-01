package com.brightfuture.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brightfuture.room.dao.WordDao
import com.brightfuture.room.entity.Word

@Database(
    entities = [Word::class],
    version = 1
)
abstract class WordDB : RoomDatabase() {
    abstract fun wordDao(): WordDao


    companion object {
        private var instance: WordDB? = null

        @Synchronized
        fun getInstance(context: Context): WordDB {
            if (instance == null) {
                instance =
                    Room.databaseBuilder(
                        context,
                        WordDB::class.java,
                        "word_db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
            }
            return instance!!
        }
    }
}