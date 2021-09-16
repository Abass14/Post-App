package com.example.week_nine_task.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.week_nine_task.R
import com.example.week_nine_task.model.PostResponseItem
import com.example.week_nine_task.viewmodel.MainViewModel

/**
 * Activity for creating new Posts
 */
class CreatePost : AppCompatActivity() {
    //Late initializing views and viewModel
    lateinit var viewModel: MainViewModel
    lateinit var title: AppCompatEditText
    lateinit var post: AppCompatEditText
    lateinit var submitPost: Button
    lateinit var backBtn: ImageButton
    lateinit var erroTxt: TextView
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        //initializing views
        title = findViewById(R.id.title_post)
        post = findViewById(R.id.postTxt)
        submitPost = findViewById(R.id.submitPost)
        backBtn = findViewById(R.id.createPostBackBtn)
        erroTxt = findViewById(R.id.errorTxt_posts)
        progressBar = findViewById(R.id.progressBar_posts)

        //initializing ViewModel and observing live data
        initCreatePostViewModel()

        //submitPost button click listener to create new post
        submitPost.setOnClickListener {
            createPost()
        }

        //submitPost button click listener to exit activity
        backBtn.setOnClickListener {
            finish()
        }
    }

    //Method to create new post
    private fun createPost(){
        if (title.text.toString().isEmpty() || post.text.toString().isEmpty()){
            Toast.makeText(this, "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            val post = PostResponseItem(1, 1, title.text.toString(), post.text.toString())
            viewModel.postPost(post)
        }
    }

    //Method to initialize ViewModel and observe live data
    private fun initCreatePostViewModel(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.postPostListLiveData.observe(this, Observer {
            if (it != null){
                progressBar.visibility = View.VISIBLE
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("newPost", it)
                startActivity(intent)
            }else{
                erroTxt.visibility = View.VISIBLE
            }
        })
    }
}