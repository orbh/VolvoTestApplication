package com.orbh.myapplication.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.orbh.myapplication.models.TaskModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialogComponent(
    showAddTaskDialog: (Boolean) -> Unit, setTasks: () -> Unit, tasks: SnapshotStateList<TaskModel>
) {

    val txtInput = remember { mutableStateOf("") }
    val errorInput = remember { mutableStateOf("") }
    var isError = false
    val focusRequester = FocusRequester()

    // Triggers at composable launch, triggering change of focus
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    fun triggerAddTask() {
        if (txtInput.value.isEmpty()) {
            errorInput.value = "Cannot be empty"
            isError = true
        } else {
            tasks.add(TaskModel(txtInput.value))
            setTasks()
            showAddTaskDialog(false)
        }
    }

    Dialog(onDismissRequest = { showAddTaskDialog(false) }) {

        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.width(250.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Add Task?")
                Text(
                    text = errorInput.value,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                TextField(placeholder = { Text(text = "Task name...") },
                    modifier = Modifier.focusRequester(focusRequester),
                    singleLine = true,
                    value = txtInput.value,
                    onValueChange = {
                        if (it.length <= 30) {
                            txtInput.value = it
                        } else {
                            errorInput.value = "Max 30 characters"
                            isError = true
                        }
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        triggerAddTask()
                    }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    isError = isError
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            showAddTaskDialog(false)
                        }) {
                        Text(text = "Cancel")
                    }
                    Button(modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        onClick = {
                            triggerAddTask()
                        }) {
                        Text(text = "Add")
                    }
                }

            }
        }
    }
}


@Preview
@Composable
fun PreviewAddTaskDialogComponent() {

    val showDialog = remember { mutableStateOf(true) }

    AddTaskDialogComponent(showAddTaskDialog = {
        showDialog.value = it
    }, setTasks = fun() {}, SnapshotStateList()
    )
}