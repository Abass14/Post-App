package com.example.week_nine_task.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.week_nine_task.model.CommentsResponseItem

@Dao
interface CommentDao {
    //function to add comments
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addComments(comment: CommentsResponseItem)

    //function to read comment data
    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<MutableList<CommentsResponseItem>>
}