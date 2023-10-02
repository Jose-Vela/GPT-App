package com.example.gptapp.model

import com.example.gptapp.CompletionData
import com.example.gptapp.CompletionResponse
import com.example.gptapp.CompletionService
import com.example.gptapp.Message
import com.example.gptapp.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CompletionInterceptor {
    fun postCompletion(prompt: String, callback: (CompletionResponse) -> Unit) {
        val service = RetrofitInstance.getRetroInstance().create(CompletionService::class.java)
        GlobalScope.launch(Dispatchers.IO) {
            try {
                var message = Message(
                    content = prompt,
                    role = "user"
                )
                val data = CompletionData(
                    List(1) { message },
                    "gpt-3.5-turbo",
                    0.7
                )
                val response = service.getCompletion(data,"Bearer YOUR_API_KEY")
                callback(response)
            } catch (e: Exception) {
                (e as? HttpException)?.let {

                }
            }
        }
    }
}