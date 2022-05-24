package com.sliide.techincaltaskapp.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

const val GoRestTOKEN = "6ec922c7bec20f3fb18dab39b2dfb63d541819b5b37caebfa3a0bcbd30842aa8"

val httpClient: OkHttpClient = OkHttpClient().newBuilder()
    .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    .addInterceptor {
        val newRequest: Request = it.request().newBuilder()
            .addHeader("Authorization", "Bearer $GoRestTOKEN")
            .build()
        it.proceed(newRequest)

    }
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .build()

object RetrofitHelper {
    private const val BASE_URL = "https://gorest.co.in/public/v2/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val goRestService: GoRestApi = getRetrofit().create(GoRestApi::class.java)

}