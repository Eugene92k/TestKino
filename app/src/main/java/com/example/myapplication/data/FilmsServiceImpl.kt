package com.example.myapplication.data

import com.example.myapplication.URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.URL


class FilmsServiceImpl : FilmsService {

    var retrofit: Retrofit
    var filmsApi: ApiFilms

    init {
        retrofit = initRetrofit()
        filmsApi = retrofit.create(ApiFilms::class.java)
    }

    private fun initRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder().apply {
            networkInterceptors().add(Interceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            })
            addInterceptor(interceptor)
        }
        return Retrofit.Builder().baseUrl(URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client.build())
            .build()

    }

    override suspend fun load() = filmsApi.getFilms()
}