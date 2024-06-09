package com.brightfuture.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.brightfuture.models.WordHistory
import com.brightfuture.models.WordFlow
import com.brightfuture.room.entity.Word
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(word: Word)

    @Update
    fun update(word: Word)

    @Query("UPDATE Word SET bookmark = :bookmark WHERE id = :wordId")
    fun updateBookmark(wordId: Long, bookmark: Int)

    @Query("UPDATE Word SET seen = :seen WHERE id = :wordId")
    fun updateSeen(wordId: Long, seen: Int)

    @Query("UPDATE Word SET searched = :searched WHERE id = :wordId")
    fun updateSearched(wordId: Long, searched: Int)

    @Query("UPDATE Word SET seen = :seen WHERE id IN (:wordIds)")
    fun updateSeenList(wordIds: List<Long>, seen: Int)

    @Query("SELECT EXISTS(SELECT * FROM Word)")
    fun isExists(): Boolean

    @Query("SELECT * FROM Word WHERE name = :name")
    fun getWordByName(name: String): Word

    @Query("SELECT * FROM Word WHERE id = :id")
    fun getWordById(id: Long): Word


    @Query("SELECT * FROM Word WHERE name = :name")
    fun getIdByName(name: String): Word


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

    @Query("SELECT id,name FROM Word WHERE seen = :seen")
    fun getAllSeenWords(seen: Int): List<WordHistory>


    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT 1")
    fun getRandomWord(): Word
    @Query("SELECT id,name FROM Word WHERE name LIKE :query || '%' LIMIT 5")
    fun searchingWords(query: String): Flow<List<WordFlow>>
}