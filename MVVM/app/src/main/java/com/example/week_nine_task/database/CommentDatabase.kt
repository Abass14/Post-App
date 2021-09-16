package com.example.week_nine_task.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.week_nine_task.model.CommentsResponseItem

@Database(entities = [CommentsResponseItem::class], version = 1, exportSchema = false)
abstract class CommentDatabase : RoomDatabase(){
    abstract fun commentDao() : CommentDao

    companion object{
        //Creating a singleton instance of the abstract class UserDatabase
        @Volatile
        private var INSTANCE: CommentDatabase? = null

        fun getDatabase(context: Context): CommentDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommentDatabase::class.java,
                    "user_database"
                ).build()
                INSTANCE = instance
                return instance
            }

        }
    }
}