package com.ahmadziya.notesapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmadziya.notesapp.network.model.Post
import com.ahmadziya.notesapp.ui.theme.CardColors

@Composable
fun PostCard(
    post: Post,
    index: Int,
    onClick: (Post) -> Unit
) {
    val cardColor = CardColors[index % CardColors.size]

    Card(
        modifier  = Modifier
            .fillMaxWidth()
            .clickable { onClick(post) },
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                // User badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                ) {
                    Text(
                        text       = "User ${post.userId}",
                        fontSize   = 10.sp,
                        color      = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium,
                        modifier   = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Text(
                    text  = "#${post.id}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text       = post.title.replaceFirstChar { it.uppercase() },
                fontSize   = 15.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onSurface,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text       = post.body,
                fontSize   = 13.sp,
                color      = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )
        }
    }
}