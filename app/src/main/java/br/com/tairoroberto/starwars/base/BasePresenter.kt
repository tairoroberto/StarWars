package br.com.tairoroberto.starwars.base

/**
 * Created by tairo on 10/29/17.
 */
interface BasePresenter<in V> {
    fun attachView(view: V)

    fun detachView()
}
