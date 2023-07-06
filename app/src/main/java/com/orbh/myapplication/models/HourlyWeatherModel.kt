package com.orbh.myapplication.models

data class HourlyWeatherModel(
    val time: Array<String>,
    val temperature_2m: Array<Number>,
    val precipitation: Array<Number>,
    val cloudcover: Array<Number>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HourlyWeatherModel

        if (!time.contentEquals(other.time)) return false
        if (!temperature_2m.contentEquals(other.temperature_2m)) return false
        if (!precipitation.contentEquals(other.precipitation)) return false
        if (!cloudcover.contentEquals(other.cloudcover)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = time.contentHashCode()
        result = 31 * result + temperature_2m.contentHashCode()
        result = 31 * result + precipitation.contentHashCode()
        result = 31 * result + cloudcover.contentHashCode()
        return result
    }
}