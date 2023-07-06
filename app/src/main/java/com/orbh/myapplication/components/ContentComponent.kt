package com.orbh.myapplication.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.gson.Gson
import com.orbh.myapplication.models.CityModel
import com.orbh.myapplication.models.TaskListModel
import com.orbh.myapplication.models.TaskModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentComponent() {

    val showAddTaskDialog = remember { mutableStateOf(value = false) }
    val tasks = remember { mutableStateListOf<TaskModel>() }
    val city: MutableState<CityModel> =
        remember { mutableStateOf(CityModel("Default", 20.00, 20.00, 10000)) }

    val context = LocalContext.current
    val gson = Gson()

    fun getTasks() {
        try {
            // Fetches saved string
            val sharedPref = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
            val json = sharedPref.getString("tasks", "")
            // Converts the saved string to JSON-object
            val tlm = gson.fromJson(json, TaskListModel::class.java)

            if (tlm != null) {
                Log.d("Tasks", "Fetching tasks")
                tasks.clear()
                for (task in tlm.tasks) {
                    tasks.add(task)
                    Log.d("Tasks", "Task: ${task.title}")
                }
            } else {
                Log.d("Tasks", "Didn't find list")
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
    }

    fun setTasks() {
        // Fetches saved string
        val sharedPref = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)
        val list = tasks.toList()
        // Creates new object from list
        val json = gson.toJson(TaskListModel(list))

        // Saves the string
        sharedPref.edit().putString("tasks", json).apply()
        Log.d("Tasks", "Saved tasks")
        Log.d("Tasks", json)
    }

    // Variable controlling the showing of "Add task"-dialog
    if (showAddTaskDialog.value) {
        AddTaskDialogComponent(
            showAddTaskDialog = {
                showAddTaskDialog.value = it
            }, setTasks = ::setTasks, tasks = tasks
        )
    }

    getTasks()

    Scaffold(floatingActionButtonPosition = FabPosition.Center, floatingActionButton = {
        FloatingActionButton(onClick = {
            showAddTaskDialog.value = true
        }) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "Add"
            )
        }
    }) { contentPadding ->
        Box(modifier = Modifier.padding(contentPadding)) {
            Column(
                modifier = Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherComponent(city = city)
                TaskListComponent(tasks = tasks, setTasks = ::setTasks)
            }
        }
    }
}

@Preview
@Composable
fun PreviewContentComponent() {
    ContentComponent()
}