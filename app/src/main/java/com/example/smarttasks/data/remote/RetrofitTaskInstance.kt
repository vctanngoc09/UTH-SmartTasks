package com.example.smarttasks.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitTaskInstance {
    private const val BASE_URL = "https://amock.io/api/researchUTH/"

    val api: ApiServiceTask by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServiceTask::class.java)
    }
}
