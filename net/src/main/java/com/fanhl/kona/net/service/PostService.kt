package com.fanhl.kona.net.service

import com.fanhl.kona.net.model.Post
import io.reactivex.Observable
import retrofit2.http.GET

interface PostService {
    @GET("/post.json")
    fun getPost(): Observable<List<Post>>
}