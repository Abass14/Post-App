package com.example.week_nine_task.database

import androidx.lifecycle.LiveData
import com.example.week_nine_task.model.CommentsResponseItem

class CommentRepository(val commentDao: CommentDao) {

    val readAllData: LiveData<MutableList<CommentsResponseItem>> = commentDao.readAllData()

    suspend fun addUser(comment: CommentsResponseItem){
        commentDao.addComments(comment)
    }
}