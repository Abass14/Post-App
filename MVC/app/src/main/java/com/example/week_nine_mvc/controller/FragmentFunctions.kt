package com.example.week_nine_mvc.controller

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.week_nine_mvc.R

object FragmentFunctions {


    //function to close fragments
    fun closeFragment(fragment: Fragment) : Boolean{
        if (fragment.isAdded){
            fragment.parentFragmentManager.beginTransaction().remove(fragment).commit()
        }
        return true
    }

    //Functions to open fragments and pass data from activity to fragment
    fun navigateFragment(fragment: Fragment, fragmentManager: FragmentManager, id: Int, postId: Int?, key:String){
        val bundle = Bundle()
        if (postId != null) {
            bundle.putInt(key, postId)
        }
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragment.arguments = bundle
        fragmentTransaction.replace(id, fragment)
        fragmentTransaction.commit()
    }
}