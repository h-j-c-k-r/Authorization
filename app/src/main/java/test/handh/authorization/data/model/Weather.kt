package test.handh.authorization.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Weather(
    val id: Int,
    val main: String,
    val description: String
)
