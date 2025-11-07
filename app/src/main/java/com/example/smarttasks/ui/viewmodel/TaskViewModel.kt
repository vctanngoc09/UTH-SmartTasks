package com.example.smarttasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttasks.data.remote.RetrofitTaskInstance
import com.example.smarttasks.data.remote.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val _tasks = MutableStateFlow<List<TaskResponse>>(emptyList())
    val tasks: StateFlow<List<TaskResponse>> = _tasks

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitTaskInstance.api.getTasks()
                if (response.isSuccess) {
                    _tasks.value = response.data
                } else {
                    _tasks.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _tasks.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }
}