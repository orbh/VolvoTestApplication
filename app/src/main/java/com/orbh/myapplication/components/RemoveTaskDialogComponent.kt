package com.orbh.myapplication.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.orbh.myapplication.models.TaskModel

@Composable
fun RemoveTaskDialogComponent(
    showRemoveTaskDialog: (Boolean) -> Unit,
    tasks: SnapshotStateList<TaskModel>,
    index: Int,
    setTasks: () -> Unit
) {

    // This is just here to enable Preview, otherwise it crashes due to it having no list
    var taskName = ""
    try {
        taskName = tasks[index].title
    } catch (e: Exception) {
        Log.d("Tasks", e.toString())
    }

    Dialog(onDismissRequest = { showRemoveTaskDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.width(250.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Remove task?")
                Text(text = "\"$taskName\"")
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            showRemoveTaskDialog(false)
                        }) {
                        Text(text = "Cancel")
                    }
                    Button(modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        onClick = {
                            try {
                                Log.d(
                                    "Tasks",
                                    "Task with name ${tasks[index].title} at index $index was removed"
                                )
                                tasks.removeAt(index)
                                Log.d("Tasks", "Current tasks:")
                                for (task in tasks) {
                                    Log.d("Tasks", "Task: ${task.title}")
                                }
                            } catch (e: Exception) {
                                Log.e("Tasks", e.toString())
                            }
                            setTasks()
                            showRemoveTaskDialog(false)

                        }) {
                        Text(text = "Remove")
                    }
                }

            }
        }
    }
}

@Preview
@Composable
fun PreviewRemoveTaskDialogComponent() {
    val showRemoveTaskDialog = remember { mutableStateOf(true) }
    RemoveTaskDialogComponent(showRemoveTaskDialog = {
        showRemoveTaskDialog.value = it
    }, tasks = SnapshotStateList(), index = 0, setTasks = fun() {})
}