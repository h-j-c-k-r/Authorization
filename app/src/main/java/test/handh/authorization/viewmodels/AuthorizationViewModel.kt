package test.handh.authorization.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import test.handh.authorization.domain.repository.IWeatherRepository
import test.handh.authorization.ui.states.WeatherState

class AuthorizationViewModel(private val weatherRepository: IWeatherRepository) : ViewModel() {

    private val _weatherState: MutableLiveData<WeatherState> = MutableLiveData(WeatherState.Idle)
    val weatherState: LiveData<WeatherState> = _weatherState

    fun getWeather(lat: Int, lon: Int) {
        _weatherState.value = WeatherState.Loading
        viewModelScope.launch {
            try {
                val result = weatherRepository.getWeather(lat, lon)
                _weatherState.value = WeatherState.Success(result)
            } catch (e: Exception) {
                _weatherState.value = e.message?.let { WeatherState.Error(it) }
            }

        }
    }
}