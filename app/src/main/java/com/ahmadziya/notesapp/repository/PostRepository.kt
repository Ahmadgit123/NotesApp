package com.ahmadziya.notesapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.ahmadziya.notesapp.network.api.RetrofitInstance
import com.ahmadziya.notesapp.network.model.Post
import com.ahmadziya.notesapp.network.model.UiState
import com.ahmadziya.notesapp.network.paging.PostPagingSource
import kotlinx.coroutines.flow.Flow

class PostRepository {

    private val api = RetrofitInstance.apiService

    fun getPagedPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PostPagingSource(api) }
        ).flow
    }

    suspend fun getAllPosts(): UiState<List<Post>> {
        return try {
            val response = api.getAllPosts()
            UiState.Success(response.posts)
        } catch (e: Exception) {
            UiState.Error(
                when {
                    e.message?.contains("Unable to resolve host") == true ->
                        "No internet connection! Please turn on WiFi or Data. 📵"
                    e.message?.contains("timeout") == true ->
                        "Server is slow. Please try again. ⏱️"
                    else ->
                        "Something went wrong: ${e.message}"
                }
            )
        }
    }
}