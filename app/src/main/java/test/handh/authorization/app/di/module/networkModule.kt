package test.handh.authorization.app.di.module

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import test.handh.authorization.BuildConfig
import test.handh.authorization.data.network.interceptors.QueryInterceptor
import test.handh.authorization.data.network.retrofit.ApiWeather
import java.util.concurrent.TimeUnit

const val TIMEOUT: Long = 30

val networkModule = module {
    single { buildRetrofit(get(), get()) }

    single { buildOkHttp(get()) }

    single { buildMoshi() }

    single { buildApiWeather(get()) }

    single { createInterceptor() }
}

private fun buildApiWeather(retrofit: Retrofit) = retrofit.create(ApiWeather::class.java)

private fun buildMoshi(): Moshi = Moshi.Builder()
    .addLast(KotlinJsonAdapterFactory())
    .build()

private fun buildOkHttp(interceptor: QueryInterceptor): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(interceptor)
    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
    .build()


private fun buildRetrofit(client: OkHttpClient, moshi: Moshi) = Retrofit.Builder()
    .baseUrl(BuildConfig.API_ENDPOINT)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

private fun createInterceptor() = QueryInterceptor()