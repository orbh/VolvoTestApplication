package com.orbh.myapplication.api

import android.util.Log
import com.google.gson.Gson
import com.orbh.myapplication.models.CityResultModel
import com.orbh.myapplication.models.DailyWeatherModel
import com.orbh.myapplication.models.HourlyWeatherModel
import com.orbh.myapplication.models.WeatherModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

class WeatherAPI {
    fun getLocationList(name: String): CityResultModel {

        var result = CityResultModel(listOf())

        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val url =
                        "https://geocoding-api.open-meteo.com/v1/search?name=$name&count=10&language=en&format=json"
                    val string = URL(url).readText()
                    val gson = Gson()
                    val crm = gson.fromJson(string, CityResultModel::class.java)
                    result = crm
                    for (city in crm.results) {
                        Log.d(
                            "API",
                            "Name: ${city.name}, Latitude: ${city.latitude}, Longitude: ${city.longitude}, Population: ${city.population}"
                        )
                    }
                } catch (e: Exception) {
                    Log.e("API", e.toString())
                }
            }
        }
        return result
    }

    fun getWeather(latitude: Number, longitude: Number): WeatherModel {

        var model = WeatherModel(
            HourlyWeatherModel(arrayOf(), arrayOf(), arrayOf(), arrayOf()),
            DailyWeatherModel(arrayOf(), arrayOf())
        )

        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    val url =
                        "https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,precipitation,cloudcover&daily=temperature_2m_max,temperature_2m_min&timezone=Europe%2FBerlin"
                    val json = URL(url).readText()
                    val gson = Gson()
                    model = gson.fromJson(json, WeatherModel::class.java)

                } catch (e: Exception) {
                    Log.e("Weather", e.toString())
                }
            }
        }
        return model
    }
}