package com.ahmadziya.notesapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.ahmadziya.notesapp.ui.components.PostCard
import com.ahmadziya.notesapp.viewmodel.PostViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsScreen(postViewModel: PostViewModel = viewModel()) {

    val pagedPosts   = postViewModel.pagedPosts.collectAsLazyPagingItems()
    val searchQuery  by postViewModel.searchQuery.collectAsState()
    val selectedPost by postViewModel.selectedPost.collectAsState()

    // Post selected → detail screen
    selectedPost?.let { post ->
        PostDetailScreen(post = post, onBack = { postViewModel.clearSelectedPost() })
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("API Posts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(
                            "Paged JSONPlaceholder API",
                            fontSize = 11.sp,
                            color    = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { pagedPosts.refresh() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // ── SEARCH BAR ────────────────────────────────────────────
            OutlinedTextField(
                value         = searchQuery,
                onValueChange = { postViewModel.onSearchQueryChange(it) },
                placeholder   = { Text("Search posts...") },
                leadingIcon   = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier      = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                shape      = RoundedCornerShape(14.dp),
                singleLine = true
            )

            // ── UI STATES ─────────────────────────────────────────────
            Box(modifier = Modifier.fillMaxSize()) {
                
                LazyColumn(
                    contentPadding      = PaddingValues(
                        start  = 16.dp, end    = 16.dp,
                        top    = 8.dp,  bottom = 16.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        count = pagedPosts.itemCount,
                        key = pagedPosts.itemKey { it.id }
                    ) { index ->
                        val post = pagedPosts[index]
                        if (post != null) {
                            androidx.compose.animation.AnimatedVisibility(
                                visible = true,
                                enter   = slideInVertically { h -> h / 2 } + fadeIn()
                            ) {
                                PostCard(
                                    post    = post,
                                    index   = index,
                                    onClick = { postViewModel.selectPost(post) }
                                )
                            }
                        }
                    }

                    // Append Loading State
                    item {
                        if (pagedPosts.loadState.append is LoadState.Loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(32.dp))
                            }
                        }
                    }

                    // Append Error State
                    item {
                        if (pagedPosts.loadState.append is LoadState.Error) {
                            val e = pagedPosts.loadState.append as LoadState.Error
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error loading more items",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Button(onClick = { pagedPosts.retry() }) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                }

                // Refresh (Initial) Loading State
                if (pagedPosts.loadState.refresh is LoadState.Loading) {
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color       = MaterialTheme.colorScheme.primary,
                                strokeWidth = 3.dp
                            )
                            Text(
                                "Fetching data from API...",
                                fontSize = 14.sp,
                                color    = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }

                // Refresh (Initial) Error State
                if (pagedPosts.loadState.refresh is LoadState.Error) {
                    val e = pagedPosts.loadState.refresh as LoadState.Error
                    Box(
                        modifier         = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            modifier            = Modifier.padding(32.dp)
                        ) {
                            Text("⚠️", fontSize = 56.sp)
                            Text(
                                "Something Went Wrong!",
                                fontSize   = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color      = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text      = e.error.localizedMessage ?: "Unknown error",
                                fontSize  = 14.sp,
                                color     = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { pagedPosts.refresh() },
                                shape   = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Try Again")
                            }
                        }
                    }
                }
            }
        }
    }
}
