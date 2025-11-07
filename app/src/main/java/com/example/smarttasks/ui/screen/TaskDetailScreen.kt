package com.example.smarttasks.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilePresent
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smarttasks.ui.components.EmptyView
import com.example.smarttasks.ui.viewmodel.TaskDetailViewModel
import kotlinx.coroutines.launch


@Composable
fun TaskDetailScreen(
    id: Int,
    onBack: () -> Unit,
    viewModel: TaskDetailViewModel = viewModel()
) {
    val task by viewModel.task.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val deleteSuccess by viewModel.deleteSuccess.collectAsState()

    // Snackbar
    val snackbarHostState = remember { SnackbarHostState() }

    // Khi xóa thành công
    LaunchedEffect(deleteSuccess) {
        if (deleteSuccess) {
            viewModel.clearTask()
            launch {
                snackbarHostState.showSnackbar("✅ Task deleted successfully")
            }
        }
    }


    // Lấy dữ liệu khi vào màn
    LaunchedEffect(id) {
        viewModel.fetchTaskById(id)
    }

    // --- UI ---
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFDE7)) // màu nền nhẹ như trong hình
            .padding(horizontal = 16.dp)
    ) {
        when {
            isLoading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF1976D2))
                }
            }

            task == null -> {
                EmptyView(onBack = onBack)
            }

            else -> {
                val data = task!!
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- HEADER ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = onBack) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF90CAF9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = Color.White
                                )
                            }
                        }

                        Text(
                            text = "Detail",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1976D2)
                        )

                        IconButton(
                            onClick = { viewModel.deleteTaskById(id) }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFCDD2)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color(0xFFD32F2F)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- TASK INFO ---
                    Text(data.title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(data.description, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CATEGORY / STATUS / PRIORITY ---
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFCDD2))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            InfoChip("Category", data.category, Icons.Default.Work)
                            InfoChip("Status", data.status)
                            InfoChip("Priority", data.priority)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- SUBTASKS ---
                    Text("Subtasks", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))

                    if (data.subtasks.isEmpty()) {
                        Text("No subtasks available", color = Color.Gray, fontSize = 13.sp)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            data.subtasks.forEach { sub ->
                                SubtaskItem(text = sub.title, checkedInit = sub.isCompleted)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // --- ATTACHMENTS ---
                    Text(
                        "Attachments",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    if (data.attachments.isEmpty()) {
                        Text("No attachments available", color = Color.Gray, fontSize = 13.sp)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            data.attachments.forEach { file ->
                                AttachmentItem(fileName = file.fileName)
                            }
                        }
                    }
                }
            }
        }

        // 🔹 Hiển thị snackbar (overlay trên Box)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}


// --- COMPONENTS --- //

@Composable
fun InfoChip(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector? = null) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = title,
                fontSize = 12.sp,
                color = Color.Black.copy(alpha = 0.7f)
            )
        }
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )
    }
}

@Composable
fun SubtaskItem(text: String, checkedInit: Boolean) {
    var checked by remember { mutableStateOf(checkedInit) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF6F6F6))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF1976D2)
            )
        )
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color.Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun AttachmentItem(fileName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Color(0xFFF2F2F2))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.FilePresent,
            contentDescription = "File",
            tint = Color(0xFF616161)
        )
        Spacer(modifier = Modifier.width(8.dp))
        ClickableText(
            text = AnnotatedString(fileName),
            onClick = { /* TODO: mở file */ },
            style = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 14.sp
            )
        )
    }
}
