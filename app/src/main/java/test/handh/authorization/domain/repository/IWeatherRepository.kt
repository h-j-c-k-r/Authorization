package test.handh.authorization.domain.repository

import test.handh.authorization.data.model.WeatherResponse

interface IWeatherRepository {
    suspend fun getWeather(lat: Int, lon: Int): WeatherResponse
}