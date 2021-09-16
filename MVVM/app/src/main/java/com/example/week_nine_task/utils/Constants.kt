package com.example.week_nine_task.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import com.example.week_nine_task.ui.CreatePost

object Constants {

    const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    fun navigate(context: Context, activity: Activity, key1: String?, key2: String?, passData1: Parcelable?, passData2: Int?){
        val intent = Intent(context, activity::class.java)
        intent.putExtra(key1, passData1)
        intent.putExtra(key2, passData2)
        context.startActivity(intent)
    }
}