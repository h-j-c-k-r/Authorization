package test.handh.authorization.data.repository

import test.handh.authorization.data.model.WeatherResponse
import test.handh.authorization.data.network.retrofit.ApiWeather
import test.handh.authorization.domain.repository.IWeatherRepository

class WeatherRepository(
    private val api: ApiWeather
): IWeatherRepository {

    override suspend fun getWeather(lat: Int, lon: Int): WeatherResponse {
        return api.getWeather(lat, lon)
    }
}