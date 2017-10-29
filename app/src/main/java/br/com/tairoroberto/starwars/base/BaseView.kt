package br.com.tairoroberto.starwars.base

import android.app.Activity
import android.content.Context

/**
 * Created by tairo on 10/29/17.
 */
interface BaseView {
    fun getContext(): Context
    fun getActivity(): Activity
}
