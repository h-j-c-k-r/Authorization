package test.handh.authorization.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MainInfo(
    val temp: Float,
    @Json(name = "feels_like")
    val feelsLike: Float
)
