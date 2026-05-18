package com.ahmadziya.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ahmadziya.notesapp.network.model.Post
import com.ahmadziya.notesapp.network.model.UiState
import com.ahmadziya.notesapp.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private val repository = PostRepository()

    // Paged posts
    val pagedPosts: Flow<PagingData<Post>> = repository.getPagedPosts()
        .cachedIn(viewModelScope)

    // Posts list state
    private val _postsState = MutableStateFlow<UiState<List<Post>>>(UiState.Loading)
    val postsState: StateFlow<UiState<List<Post>>> = _postsState

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // Selected post for detail screen
    private val _selectedPost = MutableStateFlow<Post?>(null)
    val selectedPost: StateFlow<Post?> = _selectedPost

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _postsState.value = UiState.Loading
            _postsState.value = repository.getAllPosts()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun selectPost(post: Post) {
        _selectedPost.value = post
    }

    fun clearSelectedPost() {
        _selectedPost.value = null
    }

    // Search by title or body
    fun getFilteredPosts(posts: List<Post>): List<Post> {
        val q = _searchQuery.value.trim().lowercase()
        if (q.isEmpty()) return posts
        return posts.filter {
            it.title.lowercase().contains(q) ||
                    it.body.lowercase().contains(q)
        }
    }
}