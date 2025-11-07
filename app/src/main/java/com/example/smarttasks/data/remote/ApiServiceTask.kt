package com.example.smarttasks.data.remote

import com.example.smarttasks.data.remote.BaseResponse
import com.example.smarttasks.data.remote.TaskResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServiceTask {
    @GET("tasks")
    suspend fun getTasks(): BaseResponse<List<TaskResponse>>

    @GET("task/{id}")
    suspend fun getTaskById(@Path("id") id: Int): BaseResponse<TaskResponse>

    @DELETE("task/{id}")
    suspend fun deleteTask(@Path("id") id: Int): BaseResponse<Unit>

}
