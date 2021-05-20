package test.handh.authorization.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherInfo(
    val description: String
)
