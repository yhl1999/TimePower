package com.o1.timemanager

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("/")
    fun post(@Body body: JsonObject): Call<JsonObject>
}