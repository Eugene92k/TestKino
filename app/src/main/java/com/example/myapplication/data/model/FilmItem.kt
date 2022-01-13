package com.example.myapplication.data.model

sealed class FilmItem {

    companion object {
        const val FILM = 0
        const val HEADER = 1
        const val GENRE = 2
    }
}