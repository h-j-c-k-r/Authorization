package test.handh.authorization.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class QueryInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter("appid", "4903a58c1ae880ec6859de92153fabf3")
            .addQueryParameter("lang", "ru").build()

        val newRequest = chain.request().newBuilder().url(url).build()

        return chain.proceed(newRequest)
    }
}