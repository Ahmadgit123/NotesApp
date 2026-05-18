package com.ahmadziya.notesapp.network.api

import com.ahmadziya.notesapp.network.model.Post
import com.ahmadziya.notesapp.network.model.PostResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    // GET /posts → returns PostResponse (contains list of posts)
    @GET("posts")
    suspend fun getAllPosts(): PostResponse

    // GET /posts?skip=0&limit=10 → paged posts (DummyJSON style)
    @GET("posts")
    suspend fun getPostsPaged(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): PostResponse

    // GET /posts/user/1 → posts for a specific user (DummyJSON style)
    @GET("posts/user/{userId}")
    suspend fun getPostsByUser(@Path("userId") userId: Int): PostResponse

    // GET /posts/1 → details for a single post
    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Int): Post
}