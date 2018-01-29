package com.letoti.kawa.philmaker.web

import android.content.Context

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.letoti.kawa.philmaker.BuildConfig
import com.letoti.kawa.philmaker.util.UserData

import java.util.Locale
import java.util.concurrent.TimeUnit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by akimaleo on 03.02.17.
 */
class WebManager private constructor() {
    
    private val INTERCEPTOR_QUERY = Interceptor { chain ->
        var request = chain.request()

        //LOCALIZATION
        val url = request.url().newBuilder()
                .addQueryParameter("locale", Locale.getDefault().language)
        val reqBuilder = request.newBuilder()

        //API KEY QUERY
        url.addQueryParameter("api_key", UserData.instance.accessToken)
        request = reqBuilder
                .url(url.build())
                .build()
        chain.proceed(request)
    }

    private var retrofit: Retrofit? = null
        get() {
            if (field == null) {
                field = Retrofit.Builder()
                        .baseUrl(URL_HOST)
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
            }
            return field
        }

    private val gson: Gson
        get() = GsonBuilder()
                .setLenient()
                .create()
    private var client: OkHttpClient? = null
        get() {
            if (field == null) {
                val b = OkHttpClient.Builder()
                b.connectTimeout(CONNECT_TIMEOUT.toLong(), TimeUnit.SECONDS)
                b.writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
                b.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
                b.addNetworkInterceptor(INTERCEPTOR_QUERY)
                if (BuildConfig.DEBUG) {
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.level = HttpLoggingInterceptor.Level.BODY
                    b.addInterceptor(interceptor)
                }
                field = b.build()
            }
            return field!!
        }

    companion object {
        private val URL_HOST = "https://api.themoviedb.org/4/"
        private val CONNECT_TIMEOUT = 5
        private val WRITE_TIMEOUT = 50
        private val READ_TIMEOUT = 10
    }
}