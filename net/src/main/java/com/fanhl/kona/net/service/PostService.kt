package com.fanhl.kona.net.service

import retrofit2.http.GET

interface PostService {
    @GET("/post.json")
    fun getPost()
}