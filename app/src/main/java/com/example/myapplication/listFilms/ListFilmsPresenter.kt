package com.example.myapplication.listFilms

import android.os.Bundle
import com.example.myapplication.StateView
import com.example.myapplication.data.FilmsService
import com.example.myapplication.data.model.*
import kotlinx.coroutines.*
import retrofit2.Response

class ListFilmsPresenter(private val filmsService: FilmsService) : ListFilmsContract.Presenter {

    var view: ListFilmsContract.View? = null
    var listFilm: ArrayList<Film> = arrayListOf<Film>()
    lateinit var response: Response<Films>
    override var error: String = ""
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        error = throwable.localizedMessage ?: "Loading error"
        view?.changeState(StateView.ERROR)
    }

    // Загрузка списка фильмов
    override fun load() {
        view?.changeState(StateView.LOADING)
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch(exceptionHandler) {
            response = filmsService.load()
            withContext(Dispatchers.Main + exceptionHandler) {
                    if (response.isSuccessful) {
                        listFilm = ArrayList(response?.body()?.films)
                        view?.changeState(StateView.COMPLETE)
                    } else {
                        error = response?.message().toString()
                        view?.changeState(StateView.ERROR)
                    }

            }
        }
    }

    override fun getFilmList() = listFilm

    override fun loadFromState(state: Bundle) {
        view?.changeState(StateView.LOADING)
        listFilm = state.getParcelableArrayList("listFilms") ?: arrayListOf()
        view?.changeState(StateView.COMPLETE)
    }

    // Подготовка списка для отображения в recyclerview
    override fun getFilmListUI(): ArrayList<FilmItem> {
        val genres = getGenres(listFilm)
        val data = arrayListOf<FilmItem>()
        data.add(Header(title = "Жанры"))
        data.addAll(genres)
        data.add(Header(title = "Фильмы"))
        data.addAll(listFilm)
        return data
    }

    // Получить список жанров
    private fun getGenres(listFilm: ArrayList<Film>): ArrayList<Genre> {
        val genres = arrayListOf<Genre>()
        val genresString = arrayListOf<String>()
        listFilm.forEach { filmDto ->
            filmDto.genres.forEach { genre ->
                if (!genresString.contains(genre)) {
                    genres.add(Genre(title = genre))
                    genresString.add(genre)
                }
            }
        }
        return genres
    }

    fun isViewAttached(): Boolean {
        return view != null
    }

    override fun attachView(mvpView: ListFilmsContract.View) {
        view = mvpView
    }

    override fun detachView() {
        view = null
    }

    override fun destroy() {

    }
}