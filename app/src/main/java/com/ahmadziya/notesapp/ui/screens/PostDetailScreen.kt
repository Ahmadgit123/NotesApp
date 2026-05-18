package com.ahmadziya.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmadziya.notesapp.network.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailScreen(post: Post, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Post Detail", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor         = MaterialTheme.colorScheme.primary,
                    titleContentColor      = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoChip("Post #${post.id}")
                InfoChip("User ${post.userId}")
            }

            Text(
                text       = post.title.replaceFirstChar { it.uppercase() },
                fontSize   = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

            Text(
                text       = post.body.replaceFirstChar { it.uppercase() },
                fontSize   = 15.sp,
                lineHeight = 24.sp,
                color      = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun InfoChip(label: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    ) {
        Text(
            text       = label,
            fontSize   = 12.sp,
            color      = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier   = Modifier.padding(horizontal = 12.dp, vertical = 5.dp)
        )
    }
}