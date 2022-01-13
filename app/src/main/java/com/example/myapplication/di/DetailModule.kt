package com.example.myapplication.di

import com.example.myapplication.data.FilmsService
import com.example.myapplication.data.FilmsServiceImpl
import com.example.myapplication.listFilms.ListFilmsContract
import com.example.myapplication.listFilms.ListFilmsPresenter
import org.koin.dsl.module

val appModule = module{
    factory<ListFilmsContract.Presenter> { ListFilmsPresenter(get()) }

    single<FilmsService> { FilmsServiceImpl() }
}