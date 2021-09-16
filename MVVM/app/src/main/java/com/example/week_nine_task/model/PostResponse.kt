package com.example.week_nine_task.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data class response for Posts
 */
class PostResponse : ArrayList<PostResponseItem>()

@Parcelize
data class PostResponseItem(
    val userId: Int?,
    val id: Int?,
    val title: String,
    val body: String
) : Parcelable