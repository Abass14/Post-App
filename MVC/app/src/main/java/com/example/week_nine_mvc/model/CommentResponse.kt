package com.example.week_nine_mvc.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
/**
 * Data class response for Comments
 */
class CommentsResponse : ArrayList<CommentsResponseItem>()


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
