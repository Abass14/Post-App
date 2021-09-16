package com.example.week_nine_task.ui

import android.annotation.SuppressLint
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
import com.example.week_nine_task.adapter.CommentAdapter
import com.example.week_nine_task.model.CommentsResponseItem
import com.example.week_nine_task.utils.Constants
import com.example.week_nine_task.viewmodel.MainViewModel

class PostComment : AppCompatActivity() {
    //Late-initializing views and ViewModel
    lateinit var viewModel: MainViewModel
    lateinit var name: AppCompatEditText
    lateinit var email: AppCompatEditText
    lateinit var commentBtn: Button
    lateinit var comments: AppCompatEditText
    lateinit var commentAdapter: CommentAdapter
    lateinit var commentBackBtn: ImageButton
    lateinit var postPage: PostPage
    lateinit var errorTxt: TextView
    lateinit var progressBar: ProgressBar
    var postId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_comment)
        //Initializing views
        name = findViewById(R.id.name)
        email = findViewById(R.id.email)
        commentBtn = findViewById(R.id.post)
        comments = findViewById(R.id.commentTxt)
        commentBackBtn = findViewById(R.id.commentBackBtn)
        errorTxt = findViewById(R.id.errorTxt_comment)
        progressBar = findViewById(R.id.progressBar_comment)


        commentAdapter = CommentAdapter()
        postPage = PostPage()

        //Getting passed data
        postId = intent.getIntExtra("postId", 1)
//        val postId2 = intent.getIntExtra("id2", 1)
        initPostCommentViewModel()

        commentBackBtn.setOnClickListener {
            finish()
        }
        commentBtn.setOnClickListener {
            if (postId != null){
                createComment(postId!!)
            }

        }
    }

    //Method to create Comment
    private fun createComment(id:Int){
        viewModel.postPostListLiveData
        val comment = CommentsResponseItem(
            id,
            1,
            name.text.toString(),
            email.text.toString(),
            comments.text.toString())
        if (name.text.toString().isEmpty()){
            Toast.makeText(this, "Name field can't be empty", Toast.LENGTH_SHORT).show()
        }else if (email.text.toString().isEmpty()){
            Toast.makeText(this, "Email field can't be empty", Toast.LENGTH_SHORT).show()
        }else if (comments.text.toString().isEmpty()){
            Toast.makeText(this, "Comment field can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            viewModel.postComment(id, comment)
        }

    }

    //Method to initialize viewModel and observe live data
    @SuppressLint("NotifyDataSetChanged")
    fun initPostCommentViewModel(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.postCommentLiveData.observe(this, Observer {
            progressBar.visibility = View.VISIBLE
            if (it != null){
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                Constants.navigate(this, postPage, "it", "thePostId", it, postId)
            }else{
                errorTxt.visibility = View.VISIBLE
            }
        })
    }
}