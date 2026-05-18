package com.ahmadziya.notesapp.network.model

import com.google.gson.annotations.SerializedName

data class PostResponse(
    @SerializedName("posts") val posts: List<Post>,
    @SerializedName("total") val total: Int,
    @SerializedName("skip")  val skip: Int,
    @SerializedName("limit") val limit: Int
)