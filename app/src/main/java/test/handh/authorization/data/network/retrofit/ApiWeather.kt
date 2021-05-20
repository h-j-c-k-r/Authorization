package test.handh.authorization.data.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import test.handh.authorization.data.model.WeatherResponse

interface ApiWeather {

    @GET(PATH_WEATHER)
    suspend fun getWeather(
        @Query("lat") lat: Int,
        @Query("lon") lon: Int
    ): WeatherResponse

    companion object {
        const val PATH_WEATHER = "weather"
    }
}