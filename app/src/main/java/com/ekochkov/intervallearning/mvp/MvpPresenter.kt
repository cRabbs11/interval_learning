package com.ekochkov.intervallearning.mvp

public interface MvpPresenter<V: MVPView> {
 
    fun attachView(mvpView: V)
 
    fun viewIsReady()
 
    fun detachView()
 
    fun destroy()
}