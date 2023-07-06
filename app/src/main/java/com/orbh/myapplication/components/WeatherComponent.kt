package com.orbh.myapplication.components

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.orbh.myapplication.R
import com.orbh.myapplication.api.WeatherAPI
import com.orbh.myapplication.models.CityModel
import com.orbh.myapplication.models.DailyWeatherModel
import com.orbh.myapplication.models.HourlyWeatherModel
import com.orbh.myapplication.models.WeatherModel
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun WeatherComponent(city: MutableState<CityModel>) {

    val date = remember { mutableStateOf(SimpleDateFormat.getDateInstance()) }
    val currentWeather = remember {
        mutableStateOf(
            WeatherModel(
                HourlyWeatherModel(
                    time = arrayOf(""),
                    cloudcover = arrayOf(0),
                    precipitation = arrayOf(0),
                    temperature_2m = arrayOf(0)
                ),
                DailyWeatherModel(temperature_2m_max = arrayOf(0), temperature_2m_min = arrayOf(0))
            )
        )
    }
    val showCityDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val gson = Gson()

    fun setCity() {
        // Fetches saved string
        val sharedPref = context.getSharedPreferences("city", Context.MODE_PRIVATE)
        // Creates new object from list
        val json = gson.toJson(city.value)

        // Saves the string
        sharedPref.edit().putString("city", json).apply()
        Log.d("City", "Saved city ${city.value.name}")
    }

    fun getCity() {
        try {
            // Fetches saved string
            val sharedPref = context.getSharedPreferences("city", Context.MODE_PRIVATE)
            val json = sharedPref.getString("city", "")
            // Converts the saved string to JSON-object
            val cm: CityModel = gson.fromJson(json, CityModel::class.java)

            if (!cm.name.equals(null)) {
                Log.d("City", "Fetching city")
                city.value = cm
                Log.d("City", "Currently saved city: ${cm.name}")
            } else {
                city.value = CityModel("Unspecified", 20.00, 20.00, 1000)
                Log.d("City", "Didn't find saved city")
            }
        } catch (e: NullPointerException) {
            city.value = CityModel("Unspecified", 20.00, 20.00, 1000)
            setCity()
            Log.e("Error", e.message.toString())
        }
    }

    val weatherAPI = WeatherAPI()
    getCity()
    currentWeather.value = weatherAPI.getWeather(city.value.latitude, city.value.longitude)

    if (showCityDialog.value) {
        CitySelectionComponent(showCitySelection = {
            showCityDialog.value = it
        }, city = city, setCity = ::setCity, getCity = ::getCity)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(30.dp, 15.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = date.value.format(Date()) + ", ",
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = city.value.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable(onClick = {
                    showCityDialog.value = true
                }),
                color = MaterialTheme.colorScheme.primary
            )
            Image(
                painter = painterResource(id = R.drawable.sunny),
                contentDescription = "Sun",
                modifier = Modifier.padding(10.dp),
            )
        }
        Row(
            modifier = Modifier.width(200.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWeather.value.daily.temperature_2m_min[0].toString() + "°C")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "arrow",
                    tint = Color.Blue
                )
                Text(text = "Lowest")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWeather.value.hourly.temperature_2m[0].toString() + "°C")
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Clock",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(text = "Current")
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = currentWeather.value.daily.temperature_2m_max[0].toString() + "°C")
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "arrow",
                    tint = Color.Red
                )
                Text(text = "Highest")
            }
        }
    }

}

@Preview
@Composable
fun PreviewWeatherComponent() {
    val city = remember { mutableStateOf(CityModel("Test", 20.00, 20.00, 10000)) }
    WeatherComponent(city = city)
}