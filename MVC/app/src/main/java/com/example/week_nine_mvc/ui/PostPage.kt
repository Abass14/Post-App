package com.example.week_nine_mvc.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.week_nine_mvc.R
import com.example.week_nine_mvc.adapter.CommentAdapter
import com.example.week_nine_mvc.connectivity.ConnectivityLiveData
import com.example.week_nine_mvc.controller.Controller
import com.example.week_nine_mvc.controller.FragmentFunctions
import com.example.week_nine_mvc.model.CommentsResponseItem
import com.example.week_nine_mvc.model.PostResponseItem
import com.google.android.material.button.MaterialButton

class PostPage : AppCompatActivity() {
    lateinit var userId: TextView
    lateinit var title: TextView
    lateinit var post: TextView
    lateinit var commentAdapter: CommentAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var commentBtn: MaterialButton
    private var singlePost: PostResponseItem? = null
    var singlePostId: Int? = null
    lateinit var backBtn: ImageButton
    lateinit var controller: Controller
    var newComment: CommentsResponseItem? = null
    lateinit var connectivityLiveData: ConnectivityLiveData
    lateinit var progressBar: ProgressBar
    lateinit var errorTxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_page)

        userId = findViewById(R.id.user_id_single)
        title = findViewById(R.id.title_single)
        post = findViewById(R.id.post_single)
        commentBtn = findViewById(R.id.commentBtn)
        backBtn = findViewById(R.id.backBtn)
        progressBar = findViewById(R.id.progressBar_post)
        errorTxt = findViewById(R.id.errorTxt_post)
        commentAdapter = CommentAdapter()
        recyclerView = findViewById(R.id.commentRecyclerView)
        val fragmentManager = supportFragmentManager
        val createComment = CreateComment.instance()
        connectivityLiveData = ConnectivityLiveData(application)

        controller = Controller()

        singlePost = intent.getParcelableExtra<PostResponseItem>("posts")
        singlePostId = intent.getIntExtra("pId", 1)
        newComment = intent.getParcelableExtra<CommentsResponseItem>("it")


        userId.text = singlePost?.userId.toString()
        title.text = singlePost?.title
        post.text = singlePost?.body


        backBtn.setOnClickListener {
            onBackPressed()
        }
        commentBtn.setOnClickListener {
            FragmentFunctions.navigateFragment(createComment, fragmentManager, R.id.createCommentFragment, singlePost?.id, "postId")
        }

        bindCommentUI()
       if(singlePost?.id != null){
           initGetCommentViewModel(singlePost?.id!!)
       }else{
           initGetPostViewModel(singlePostId!!)
           initGetCommentViewModel(singlePostId!!)
       }

    }

    fun bindPostUI(posts: PostResponseItem){
        userId.text = posts.userId.toString()
        title.text = posts.title
        post.text = posts.body
    }

    fun bindCommentUI(){
        recyclerView.apply {
            adapter = commentAdapter
            layoutManager = LinearLayoutManager(this@PostPage)
        }
    }

    fun initGetPostViewModel(id: Int) {
        controller.singlePostLiveData.observe(this, Observer {
            if (it != null){
                bindPostUI(it)
            }else{
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        })
        controller.getSinglePost(id)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initGetCommentViewModel(id: Int){
        controller.getCommentLiveData.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null){
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
                    controller.getComment(id)
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

}