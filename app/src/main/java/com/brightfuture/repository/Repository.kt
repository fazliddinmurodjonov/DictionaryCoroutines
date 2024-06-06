package com.brightfuture.repository

import android.util.Log
import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
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

    suspend fun getWord(): Resource<Boolean> {
        return Resource.success(true)
    }
}