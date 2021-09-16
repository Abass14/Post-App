package com.example.week_nine_task.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

class CommentsResponse : ArrayList<CommentsResponseItem>()

/**
 * Data class response for Comments
 */
@Parcelize
@Entity(tableName = "user_table")
data class CommentsResponseItem(
    val postId: Int,
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val email: String,
    val body: String
) : Parcelable
