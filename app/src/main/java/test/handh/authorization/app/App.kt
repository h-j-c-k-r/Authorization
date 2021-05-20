package test.handh.authorization.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import test.handh.authorization.app.di.module.networkModule
import test.handh.authorization.app.di.module.repositoryModule
import test.handh.authorization.app.di.module.viewModelModule

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}