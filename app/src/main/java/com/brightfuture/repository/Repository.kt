package com.brightfuture.repository

import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

object Repository {

    suspend fun getWords(): Resource<Pair<Boolean, Boolean>> {
        var count = 0
        return try {
            coroutineScope {
                for (word in Functions.essentialWords) {
                    ++count
                    val response = async {
                        Functions.service.getWord(
                            word
                        )
                    }.await()
                    if (response.isSuccessful) {
                        Functions.insertWord(response.body()!![0])
                        Resource.success(Pair(true, false))
                    } else {
                        Resource.success(Pair(true, false))
                    }
                }
                Resource.success(Pair(true, true))
            }
        } catch (e: IOException) {
            Resource.error("Network error: $e", Pair(false, false))
        } catch (e: Exception) {
            Resource.error("Unexpected error: $e", Pair(false, false))
        }
    }

    suspend fun getWord(): Resource<Boolean> {
        return Resource.success(true)
    }
}