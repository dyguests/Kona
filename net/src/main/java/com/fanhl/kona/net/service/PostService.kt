package com.fanhl.kona.net.service

import io.reactivex.Observable
import retrofit2.http.GET

interface PostService {
    @GET("/post.json")
    fun getPost(): Observable<Any>
}