package com.example.week_nine_mvc.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RetroService {
    @GET("posts")
    fun getPost() : Call<PostResponse>

    @POST("posts")
    fun createPost(@Body params: PostResponseItem) : Call<PostResponseItem>

    @GET("posts/{post_id}")
    fun getSinglePost(@Path("post_id") postId: Int) : Call<PostResponseItem>

    @GET("posts/{post_id}/comments")
    fun getComments(@Path("post_id") postId: Int) : Call<CommentsResponse>

    @POST("posts/{post_id}/comments")
    fun postComments(@Body params: CommentsResponseItem, @Path("post_id") postId: Int) : Call<CommentsResponseItem>
}