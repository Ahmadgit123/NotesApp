package com.ahmadziya.notesapp.network.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ahmadziya.notesapp.network.api.ApiService
import com.ahmadziya.notesapp.network.model.Post

class PostPagingSource(
    private val apiService: ApiService
) : PagingSource<Int, Post>() {

    override fun getRefreshKey(state: PagingState<Int, Post>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Post> {
        val page = params.key ?: 0 // DummyJSON uses skip, so we can track by skip or page index. Let's use skip.
        val skip = page
        val limit = params.loadSize

        return try {
            val response = apiService.getPostsPaged(skip, limit)
            val posts = response.posts
            
            LoadResult.Page(
                data = posts,
                prevKey = if (skip == 0) null else skip - limit,
                nextKey = if (posts.isEmpty() || skip + limit >= response.total) null else skip + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
