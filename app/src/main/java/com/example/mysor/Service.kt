package com.example.mysor

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


internal interface Service {
    @Multipart
    @POST("/yourEndPoint")
    fun postImage(@Part image: MultipartBody.Part?): Call<ResponseBody?>?
}