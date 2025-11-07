package com.example.smarttasks.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarttasks.R
import com.example.smarttasks.data.remote.TaskResponse
import com.example.smarttasks.ui.components.EmptyView
import com.example.smarttasks.ui.viewmodel.TaskViewModel

@Composable
fun Home(viewModel: TaskViewModel = viewModel(),
         onTaskClick: (Int) -> Unit) {
    val tasks by viewModel.tasks.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    when {
        isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF1976D2))
            }
        }
        tasks.isEmpty() -> EmptyView()
        else -> TaskList(tasks, onTaskClick)
    }
}

@Composable
fun TaskList(tasks: List<TaskResponse>, onTaskClick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.img_uth),
                    contentDescription = "Logo UTH",
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp)),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "SmartTasks",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1976D2)
                    )
                    Text(
                        text = "A simple and efficient to-do app",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔹 Danh sách task
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tasks) { task ->
                TaskCard(task, onClick = onTaskClick)
            }
        }
    }
}

@Composable
fun TaskCard(task: TaskResponse, onClick: (Int) -> Unit) {
    val color = when (task.status) {
        "In Progress" -> Color(0xFFFFCDD2)
        "Pending" -> Color(0xFFF0F4C3)
        else -> Color(0xFFB3E5FC)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(task.id) }   // ✅ thêm click event
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(task.title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(task.description, fontSize = 13.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Status: ${task.status}", fontSize = 13.sp)
                Text(task.dueDate.substringBefore("T"), fontSize = 13.sp, color = Color.Gray)
            }
        }
    }
}
