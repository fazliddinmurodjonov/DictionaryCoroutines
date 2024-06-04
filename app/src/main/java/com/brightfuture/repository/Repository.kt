package com.brightfuture.repository

import com.brightfuture.utils.Functions
import com.brightfuture.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.IOException

object Repository {

    suspend fun getWords(): Resource<Pair<Boolean, Boolean>> {
        return try {
            coroutineScope {
                for (word in Functions.essentialWords) {
                    val response = async {
                        Functions.service.getWord(
                            word
                        )
                    }.await()
                    if (response.isSuccessful) {
                        Functions
                        Resource.success(Pair(true, true))
                    }
                }


                if (response.isSuccessful) {

                    //   Resource.success(Pair(locationData, response.body()!!.address.country_code))
                    Resource.success(Pair(true, true))
                } else {
                    //Resource.error("Response code : ${response.code()}", Pair(locationData, ""))
                    Resource.success(Pair(true, true))

                }
            }
        } catch (e: IOException) {
            //   Resource.error("Network error: $e", Pair(locationData, ""))
            Resource.success(Pair(true, true))

        } catch (e: Exception) {
            //       Resource.error("Unexpected error: $e", Pair(locationData, ""))
            Resource.success(Pair(true, true))
        }
    }

    suspend fun getWord(): Resource<Boolean> {
        return Resource.success(true)
    }
}