package com.example.smarttasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smarttasks.data.remote.RetrofitTaskInstance
import com.example.smarttasks.data.remote.TaskResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskDetailViewModel : ViewModel() {
    private val _task = MutableStateFlow<TaskResponse?>(null)
    val task: StateFlow<TaskResponse?> = _task

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess: StateFlow<Boolean> = _deleteSuccess

    fun fetchTaskById(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitTaskInstance.api.getTaskById(id)
                if (response.isSuccess) _task.value = response.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTaskById(id: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitTaskInstance.api.deleteTask(id)
                if (response.isSuccess) {
                    _deleteSuccess.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearTask() {
        _task.value = null
    }

}
