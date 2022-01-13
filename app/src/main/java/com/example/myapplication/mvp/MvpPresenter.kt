package com.example.myapplication.mvp

interface MvpPresenter<V: MvpView> {
    fun attachView(mvpView: V)
    fun detachView()
    fun destroy()
}