package com.sliide.techincaltaskapp.network

import com.sliide.techincaltaskapp.data.User
import retrofit2.Response
import retrofit2.http.*

interface GoRestApi {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int = 0): List<User>

    @DELETE("users/{id}")
    suspend fun removeUser(@Path("id") id: Int) : Response<Unit>

    @POST("users")
    suspend fun addUser(@Body user: User) : User

}