package com.example.week_nine_task.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week_nine_task.R
import com.example.week_nine_task.adapter.CommentAdapter
import com.example.week_nine_task.model.CommentsResponseItem
import com.example.week_nine_task.model.PostResponseItem
import com.example.week_nine_task.network.ConnectivityLiveData
import com.example.week_nine_task.viewmodel.MainViewModel

class PostPage : AppCompatActivity() {
    //Late-initializing views
    lateinit var viewModel: MainViewModel
    lateinit var userId: TextView
    lateinit var title: TextView
    lateinit var post: TextView
    lateinit var back: ImageButton
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var commentBtn: ImageButton
    var newComment: CommentsResponseItem? = null
    lateinit var progressBar: ProgressBar
    lateinit var errorTxt: TextView
    lateinit var connectivityLiveData: ConnectivityLiveData

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_page)
        //initializing views
        userId = findViewById(R.id.user_id_single)
        title = findViewById(R.id.title_single)
        post = findViewById(R.id.post_single)
        back = findViewById(R.id.backBtn)
        commentBtn = findViewById(R.id.commentBtn)
        progressBar = findViewById(R.id.progressBar_post)
        errorTxt = findViewById(R.id.errorTxt_post)
        commentAdapter = CommentAdapter()
        connectivityLiveData = ConnectivityLiveData(application)
        recyclerView = findViewById(R.id.commentRecyclerView)

        //back button click listener to exit activity
        back.setOnClickListener {
            finish()
        }

        val idTwo = intent.getIntExtra("thePostId", 1)
        val id = intent.getIntExtra("id", idTwo)
        newComment = intent.getParcelableExtra<CommentsResponseItem>("it")

        commentBtn.setOnClickListener {
            navigationToPostComment(id)
        }

        bindCommentUI()

        initGetCommentViewModel(id)
        initGetPostViewModel(id)


        //readComment()
    }

    //Method to navigate to PostComment Activity
    private fun navigationToPostComment(id: Int){
        val intent = Intent(this, PostComment::class.java)
        intent.putExtra("postId", id)
        startActivity(intent)
    }

    //Method to bind views with Model
    private fun bindPostUI(posts: PostResponseItem){
        userId.text = posts.userId.toString()
        title.text = posts.title
        post.text = posts.body
    }

    private fun bindCommentUI(){
        recyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@PostPage)
        }
    }

    //Method to observe post live data and bind response to UI
    private fun initGetPostViewModel(id: Int) {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.singlePostLiveData.observe(this, Observer {
            if (it != null){
                bindPostUI(it)
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        })

        connectivityLiveData.observe(this, Observer {  isAvailable ->
            progressBar.visibility = View.GONE
            when (isAvailable) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    viewModel.getSinglePost(id)
                    errorTxt.visibility = View.GONE
                }
                false -> {
                    errorTxt.text = getString(R.string.offline)
                    errorTxt.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        })

    }

    //method to observe comments live data response and bind to recyclerView Adapter
    @SuppressLint("NotifyDataSetChanged")
    fun initGetCommentViewModel(id: Int){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.getCommentLiveData.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null){
//                for (i in it){
//                    viewModel.addComment(i)
//                }
                commentAdapter.commentList = it.toMutableList()
                if (newComment != null){
                    commentAdapter.commentList.add(newComment!!)
                }
                commentAdapter.notifyDataSetChanged()
            }else{
                errorTxt.visibility = View.VISIBLE
            }
        })

        connectivityLiveData.observe(this, Observer { isAvailable ->
            progressBar.visibility = View.GONE
            when (isAvailable) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    viewModel.getComment(id)
                    errorTxt.visibility = View.GONE
                }
                false -> {
                    errorTxt.text = getString(R.string.offline)
                    errorTxt.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
            }
        })
    }

    //Method to read comments from database and bind with recyclerview
    fun readComment(){
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.readAlData.observe(this, {
            commentAdapter.setData(it)
        })
    }
}