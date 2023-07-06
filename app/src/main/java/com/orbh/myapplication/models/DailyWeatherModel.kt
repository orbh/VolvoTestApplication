package com.orbh.myapplication.models

data class DailyWeatherModel(
    val temperature_2m_max: Array<Number>, val temperature_2m_min: Array<Number>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DailyWeatherModel

        if (!temperature_2m_max.contentEquals(other.temperature_2m_max)) return false
        if (!temperature_2m_min.contentEquals(other.temperature_2m_min)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = temperature_2m_max.contentHashCode()
        result = 31 * result + temperature_2m_min.contentHashCode()
        return result
    }
}