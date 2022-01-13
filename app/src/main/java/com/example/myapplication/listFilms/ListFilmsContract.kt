package com.example.myapplication.listFilms

import android.os.Bundle
import com.example.myapplication.StateView
import com.example.myapplication.data.model.Film
import com.example.myapplication.data.model.FilmItem
import com.example.myapplication.mvp.MvpPresenter
import com.example.myapplication.mvp.MvpView

interface ListFilmsContract {
    interface Presenter: MvpPresenter<View> {
        var error: String
        fun load()
        fun loadFromState(state: Bundle)
        fun getFilmList(): ArrayList<Film>
        fun getFilmListUI(): ArrayList<FilmItem>
    }
    interface View: MvpView {
        fun changeState(state: StateView)
    }
}