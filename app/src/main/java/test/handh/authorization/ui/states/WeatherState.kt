package test.handh.authorization.ui.states

import test.handh.authorization.data.model.WeatherResponse

sealed class WeatherState {
    data class Success(val response: WeatherResponse): WeatherState()
    object Loading: WeatherState()
    object Idle: WeatherState()
    data class Error(val message: String): WeatherState()
}
