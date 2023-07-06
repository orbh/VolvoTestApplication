package com.orbh.myapplication.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.orbh.myapplication.api.WeatherAPI
import com.orbh.myapplication.models.CityModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CitySelectionComponent(showCitySelection: (Boolean) -> Unit, city: MutableState<CityModel>, setCity: () -> Unit, getCity: () -> Unit) {

    val txtInput = remember { mutableStateOf("") }
    val errorInput = remember { mutableStateOf("") }
    val locations = remember { mutableStateListOf<CityModel>() }
    var isError = false
    val focusRequester = FocusRequester()
    val api = WeatherAPI()

    // Triggers at composable launch, triggering change of focus
    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
    }

    Dialog(onDismissRequest = { showCitySelection(false) }) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(250.dp)
                .height(400.dp)
        ) {
            Column(
                modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(text = "Set location")
                Text(
                    text = errorInput.value,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                TextField(
                    placeholder = { Text(text = "Location...") },
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
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                    isError = isError
                )
                Button(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {
                    if (txtInput.value != "") {
                        val list = api.getLocationList(
                            name = txtInput.value.trimEnd()
                        )
                        try {
                            if (list.results.isNotEmpty()) {
                                locations.clear()
                                for (item in list.results) {
                                    Log.d("City", "Added ${item.name} to list of cities")
                                    locations.add(item)
                                }
                            }
                        } catch (e: NullPointerException) {
                            errorInput.value = "Could not find city"
                            Log.e("City", e.toString())
                        }
                    } else {
                        errorInput.value = "Please enter name of city"
                    }
                }) {
                    Text(text = "Search")
                }
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "City", style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Population", style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .height(2.dp)
                            .background(color = Color.Gray)
                    )
                }

                LazyColumn {
                    items(locations.size) { i ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(10.dp)
                                .clickable {
                                    city.value = locations[i]
                                    Log.d("City", "Set city to ${locations[i]}")
                                    setCity()
                                    getCity()
                                    showCitySelection(false)
                                }
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = locations[i].name,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = locations[i].population.toString(),
                                    style = MaterialTheme.typography.bodySmall
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
}


@Preview
@Composable
fun PreviewCitySelectionComponent() {
    CitySelectionComponent(
        showCitySelection = ({}),
        city = remember { mutableStateOf(CityModel("Eskilstuna", 20.00, 20.00, 10000)) },
        setCity = {},
        getCity = {})
}
