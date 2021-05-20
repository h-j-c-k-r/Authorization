package test.handh.authorization.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WindInfo(
    val speed: Float
)
