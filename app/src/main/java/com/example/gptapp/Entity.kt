package com.example.gptapp

data class CompletionData(
    val messages: List<Message>,
    val model: String,
    val temperature: Double
)

data class Message(
    val content: String,
    val role: String
)

data class CompletionResponse(
    val choices: List<Choice>,
    val created: Int,
    val id: String,
    val model: String,
    val `object`: String,
    val usage: Usage
)

data class Choice(
    val finish_reason: String,
    val index: Int,
    val message: Message
)

data class Usage(
    val completion_tokens: Int,
    val prompt_tokens: Int,
    val total_tokens: Int
)