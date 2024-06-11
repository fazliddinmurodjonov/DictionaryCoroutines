package com.brightfuture.repository

import android.util.Log
import com.brightfuture.models.word_response.WordResponse
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

object Repository {

    fun getWords(): Flow<Resource<Pair<Boolean, Boolean>>> = flow {
        try {
            for (word in Functions.essentialWords) {
                val response = Functions.service.getWord(word)
                if (response.isSuccessful) {
                    Functions.insertWord(response.body()!![0])
                    emit(Resource.success(Pair(true, false)))
                } else {
                    emit(Resource.success(Pair(true, false)))
                }
            }
            emit(Resource.success(Pair(true, true)))
        } catch (e: IOException) {
            emit(Resource.error("Network error: $e", Pair(false, false)))
        } catch (e: Exception) {
            emit(Resource.error("Unexpected error: $e", Pair(false, false)))
        }
    }

    suspend fun getWord(wordName: String): Resource<Long> {
        var id: Long = 0
        return try {
            coroutineScope {
                val getWordResponse = async {
                    Functions.service.getWord(wordName)
                }.await()
                if (getWordResponse.isSuccessful) {
                    id = Functions.insertWord(getWordResponse.body()!![0])
                    Resource.success(id)
                } else {
                    Resource.error("Word isn't found", id)
                }
            }
        } catch (e: IOException) {
            Resource.error("Network error: $e", id)
        } catch (e: Exception) {
            Resource.error("Unexpected error: $e", id)
        }
    }
}