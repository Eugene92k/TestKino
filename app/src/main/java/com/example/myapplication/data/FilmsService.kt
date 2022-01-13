package com.example.myapplication.data

import com.example.myapplication.data.model.Films
import retrofit2.Response

interface FilmsService {
    suspend fun load(): Response<Films>
}