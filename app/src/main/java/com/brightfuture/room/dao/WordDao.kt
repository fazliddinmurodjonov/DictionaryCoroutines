package com.brightfuture.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.brightfuture.room.entity.Word
import io.reactivex.rxjava3.core.Flowable

@Dao
interface WordDao {
    @Insert
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Query("UPDATE Word SET bookmark = :bookmark WHERE id = :wordId")
    fun updateBookmark(wordId: Long, bookmark: Int)

    @Query("UPDATE Word SET seen = :seen WHERE id = :wordId")
    fun updateSeen(wordId: Long, seen: Int)

    @Query("SELECT EXISTS(SELECT * FROM Word)")
    fun isExists(): Boolean

    @Query("SELECT * FROM Word WHERE name = :name")
    fun getWordByName(name: String): Word

    @Query("SELECT * FROM Word")
    fun getAllWords(): List<Word>

    @Query("SELECT COUNT(*) FROM Word")
    fun getCountOfWords(): Int

    @Query("SELECT * FROM Word WHERE searched = :searched")
    fun getAllSearchedWords(searched: Int): List<Word>

    @Query("SELECT * FROM Word WHERE bookmark = :bookmark")
    fun getAllBookmarkWords(bookmark: Int): List<Word>

    @Query("SELECT * FROM Word WHERE bookmark = :bookmark")
    fun getAllBookmarkWordsFlowable(bookmark: Int): Flowable<List<Word>>

    @Query("SELECT * FROM Word WHERE seen = :seen")
    fun getAllSeenWords(seen: Int): List<Word>

    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(): Word

}