package com.ahmadziya.notesapp.network.model

import com.google.gson.annotations.SerializedName

// JSON returned from JSONPlaceholder API:
// { "id": 1, "userId": 1, "title": "...", "body": "..." }

data class Post(
    @SerializedName("id")     val id: Int,
    @SerializedName("userId") val userId: Int,
    @SerializedName("title")  val title: String,
    @SerializedName("body")   val body: String
)