package com.fanhl.http.api

import com.rxhttp.RxHttp
import com.rxhttp.annotation.Domain
import com.rxhttp.annotation.Param
import io.reactivex.rxjava3.core.Observable

@Domain("https://api.example.com")
interface ExampleApi {
    @GET("/users")
    fun getUsers(): Observable<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: String): Observable<User>

    @POST("/users")
    fun createUser(@Body user: User): Observable<User>

    @PUT("/users/{id}")
    fun updateUser(
        @Path("id") id: String,
        @Body user: User
    ): Observable<User>

    @DELETE("/users/{id}")
    fun deleteUser(@Path("id") id: String): Observable<Unit>
}

// Example data class
data class User(
    val id: String,
    val name: String,
    val email: String
) 