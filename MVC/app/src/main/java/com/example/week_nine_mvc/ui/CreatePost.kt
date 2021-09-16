package com.example.week_nine_mvc.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.week_nine_mvc.R
import com.example.week_nine_mvc.controller.Controller
import com.example.week_nine_mvc.controller.FragmentFunctions
import com.example.week_nine_mvc.model.PostResponseItem

/**
 * Fragment for creating new Posts
 */
class CreatePost : Fragment() {
    //Late initializing views and controller
    lateinit var title: EditText
    lateinit var post: AppCompatEditText
    lateinit var submitBtn: Button
    lateinit var backBtn: ImageButton
    lateinit var controller: Controller

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_post, container, false)
        //initializing views
        title = view.findViewById(R.id.title_post)
        post = view.findViewById(R.id.postTxt)
        submitBtn = view.findViewById(R.id.submitPost)
        backBtn = view.findViewById(R.id.createPostBackBtn)
        controller = Controller()

        initCreatePostViewModel()

        //submitBtn button click listener to create new post
        submitBtn.setOnClickListener {
            createPost()
        }

        //back button click listener to exit activity
        backBtn.setOnClickListener {
            FragmentFunctions.closeFragment(this)
        }
        return view
    }

    //Method to create new post
    private fun createPost(){
        if (title.text.toString().isEmpty() || post.text.toString().isEmpty()){
            Toast.makeText(requireActivity(), "Fields can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            val post = PostResponseItem(1, 1, title.text.toString(), post.text.toString())
            controller.postPost(post)
        }

    }

    //Method to initialize ViewModel and observe live data
    private fun initCreatePostViewModel(){

        controller.postPostListLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                Log.d("CreatePost", "$it")

                val intent = Intent(activity?.baseContext, MainActivity::class.java)
                intent.putExtra("newPost", it)
                activity?.startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
                Log.d("CreatePost", "Failed: $it")
            }
        })
    }

    //Fragment singleton instance
    companion object{
        fun instance() = CreatePost()
    }

}