package com.example.gptapp

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CompletionService {

    @POST("chat/completions")
    suspend fun getCompletion(
        @Body completionData: CompletionData,
        @Header("Authorization") barrer: String
    ): CompletionResponse
}