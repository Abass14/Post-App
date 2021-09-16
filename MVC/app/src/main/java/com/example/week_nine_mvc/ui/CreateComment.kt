package com.example.week_nine_mvc.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.lifecycle.Observer
import com.example.week_nine_mvc.R
import com.example.week_nine_mvc.adapter.CommentAdapter
import com.example.week_nine_mvc.controller.Controller
import com.example.week_nine_mvc.controller.FragmentFunctions
import com.example.week_nine_mvc.model.CommentsResponseItem

class CreateComment : Fragment() {
    //Late-initializing views and controller
    lateinit var name: AppCompatEditText
    lateinit var email: AppCompatEditText
    lateinit var commentBtn: Button
    lateinit var comments: AppCompatEditText
    lateinit var commentBackBtn: ImageButton
    lateinit var controller: Controller
    var postId: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_comment, container, false)
        //Initializing views
        name = view.findViewById(R.id.name)
        email = view.findViewById(R.id.email)
        commentBtn = view.findViewById(R.id.createCommentBtn)
        comments = view.findViewById(R.id.commentTxt)
        commentBackBtn = view.findViewById(R.id.commentBackBtn)
        controller = Controller()

        //Getting passed data
        postId = this.arguments?.getInt("postId")
        Log.d("CreateComment", postId.toString())

        commentBackBtn.setOnClickListener {
            FragmentFunctions.closeFragment(this)
        }

        initPostCommentViewModel()
        commentBtn.setOnClickListener {
            if (postId != null){
                postId?.let { createComment(it) }
            }

        }
        return view
    }

    //Fragment singleton instance
    companion object{
        fun instance() = CreateComment()
    }

    //Method to create Comment
    private fun createComment(id:Int){
        controller.postPostListLiveData
        val comment = CommentsResponseItem(
            id,
            1,
            name.text.toString(),
            email.text.toString(),
            comments.text.toString())
        if (name.text.toString().isEmpty()){
            Toast.makeText(requireActivity(), "Name field can't be empty", Toast.LENGTH_SHORT).show()
        }else if (email.text.toString().isEmpty()){
            Toast.makeText(requireActivity(), "Email field can't be empty", Toast.LENGTH_SHORT).show()
        }else if (comments.text.toString().isEmpty()){
            Toast.makeText(requireActivity(), "Comment field can't be empty", Toast.LENGTH_SHORT).show()
        }else{
            controller.postComment(id, comment)
        }

    }

    //Method to initialize viewModel and observe live data
    @SuppressLint("NotifyDataSetChanged")
    fun initPostCommentViewModel(){
        controller.postCommentLiveData.observe(viewLifecycleOwner, Observer {
            if (it != null){
                Toast.makeText(requireActivity(), "Success", Toast.LENGTH_SHORT).show()
                val intent = Intent(activity?.baseContext, PostPage::class.java)
                intent.putExtra("it", it)
                intent.putExtra("pId", postId)
                activity?.startActivity(intent)
            }else{
                Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}