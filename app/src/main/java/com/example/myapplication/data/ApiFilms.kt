package com.example.myapplication.data

import com.example.myapplication.data.model.Films
import retrofit2.Response
import retrofit2.http.GET

interface ApiFilms {

    @GET("sequeniatesttask/films.json")
    suspend fun getFilms() : Response<Films>
}