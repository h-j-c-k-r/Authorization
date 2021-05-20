package test.handh.authorization.app.di.module

import org.koin.dsl.module
import test.handh.authorization.data.repository.WeatherRepository
import test.handh.authorization.domain.repository.IWeatherRepository

val repositoryModule = module {
    single<IWeatherRepository> { WeatherRepository(get()) }
}
