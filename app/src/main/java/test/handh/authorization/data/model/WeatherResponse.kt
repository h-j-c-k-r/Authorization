package test.handh.authorization.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val weather: List<Weather>,
    val main: MainInfo,
    val wind: WindInfo,
    val name: String
)