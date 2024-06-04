package com.brightfuture.retrofit

import com.brightfuture.models.word_response.WordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("entries/en/{word}")
    suspend fun getWord(@Path("word") word: String): Response<WordResponse>
}