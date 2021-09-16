package com.example.week_nine_mvc.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class PostResponse : ArrayList<PostResponseItem>()
/**
 * Data class response for Posts
 */
@Parcelize
data class PostResponseItem(
    val userId: Int?,
    val id: Int?,
    val title: String,
    val body: String
) : Parcelable