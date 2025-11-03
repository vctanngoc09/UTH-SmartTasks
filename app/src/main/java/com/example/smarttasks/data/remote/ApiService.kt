package com.example.smarttasks.data.remote

import com.example.smarttasks.data.model.Product
import retrofit2.http.GET

interface ApiService {
    @GET("v2/product")
    suspend fun getProduct(): Product
}