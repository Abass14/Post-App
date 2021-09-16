package com.example.week_nine_mvc.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.week_nine_mvc.R
import com.example.week_nine_mvc.adapter.PostAdapter
import com.example.week_nine_mvc.connectivity.ConnectivityLiveData
import com.example.week_nine_mvc.controller.Controller
import com.example.week_nine_mvc.controller.FragmentFunctions
import com.example.week_nine_mvc.model.PostResponseItem

class MainActivity : AppCompatActivity() {
    //Late-initializing views and controller
    lateinit var recyclerView: RecyclerView
    lateinit var postAdapter: PostAdapter
    lateinit var progressBar: ProgressBar
    lateinit var errorTxt: TextView
    lateinit var floatBtn: ImageButton
    lateinit var searchTxt: AppCompatEditText
    lateinit var searchBtn: ImageButton
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    var newPost: PostResponseItem? = null
    lateinit var controller: Controller
    lateinit var connectivityLiveData: ConnectivityLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initializing views
        errorTxt = findViewById(R.id.errorTxt_main)
        floatBtn = findViewById(R.id.floatBtn)
        searchBtn = findViewById(R.id.searchBtn)
        searchTxt = findViewById(R.id.searchTxt)
        swipeRefreshLayout = findViewById(R.id.swipeLayout)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar_main)
        val createPost = CreatePost.instance()
        controller = Controller()
        val fragmentManager = supportFragmentManager
        connectivityLiveData = ConnectivityLiveData(application)

        //Getting transferred data from CreatePost Fragment
        newPost = intent.getParcelableExtra("newPost")
        Log.d("Main", newPost.toString())

        postAdapter = PostAdapter(this)
        bindUI()
        initViewModel()

        //submitPost button click listener to open CreatePost
        floatBtn.setOnClickListener {
            FragmentFunctions.navigateFragment(createPost, fragmentManager, R.id.createPostFragment, null, "postsId")
        }

        //submitPost button click listener to search Posts
        searchBtn.setOnClickListener {
            search(searchTxt.text.toString())
        }

        //submitPost button refresh listener to refresh API call
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = false
            initViewModel()

        }
    }

    //Method to search for Post
    @SuppressLint("NotifyDataSetChanged")
    fun search(searchTxt: String) {
        postAdapter.postList =  postAdapter.postList.filter {
            it.title.contains(searchTxt, ignoreCase = false)
        } as ArrayList<PostResponseItem>

        postAdapter.notifyDataSetChanged()

    }

    private fun bindUI(){
        recyclerView.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    //Method for initializing ViewModel and observing livedata
    @SuppressLint("NotifyDataSetChanged")
    fun initViewModel(){
        controller.getPostListLiveData.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null){
                postAdapter.postList = it
                if (newPost != null){
                    postAdapter.postList.add(newPost!!)
                }
                postAdapter.notifyDataSetChanged()
            }else{
                errorTxt.visibility = View.VISIBLE
            }
        })
        connectivityLiveData.observe(this, Observer {  isAvailable ->
            progressBar.visibility = View.GONE
            when (isAvailable) {
                true -> {
                    progressBar.visibility = View.VISIBLE
                    controller.getPost()
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

