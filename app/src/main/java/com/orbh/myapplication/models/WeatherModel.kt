package com.orbh.myapplication.models

data class WeatherModel(
    val hourly: HourlyWeatherModel, val daily: DailyWeatherModel
)