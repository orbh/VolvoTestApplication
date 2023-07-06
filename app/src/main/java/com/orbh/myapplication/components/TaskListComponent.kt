package com.orbh.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orbh.myapplication.models.TaskModel

@Composable
fun TaskListComponent(tasks: SnapshotStateList<TaskModel>, setTasks: () -> Unit) {

    val showDeleteTaskDialog = remember { mutableStateOf(value = false) }
    val currentIndex = remember { mutableStateOf(value = 0) }

    // Variable controlling the showing of "Remove task"-dialog
    // ToDo Not finished
    if (showDeleteTaskDialog.value) {
        RemoveTaskDialogComponent(
            tasks = tasks, index = currentIndex.value, showRemoveTaskDialog = {
                showDeleteTaskDialog.value = it
            }, setTasks = setTasks
        )
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks.size) { i ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = tasks[i].title, style = MaterialTheme.typography.titleMedium
                        )
                        Button(colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                            onClick = {
                                currentIndex.value = i
                                showDeleteTaskDialog.value = true
                            }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(2.dp)
                            .background(color = Color.LightGray)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTaskListComponent() {
    val previewTasks = { mutableStateListOf(TaskModel("Test"), TaskModel("Hejdå")) }
    TaskListComponent(tasks = previewTasks(), setTasks = fun() {})
}