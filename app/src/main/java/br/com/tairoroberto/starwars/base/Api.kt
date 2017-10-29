package br.com.tairoroberto.starwars.base

import br.com.tairoroberto.starwars.model.Person
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by tairo on 10/30/17.
 */
interface Api {
    @GET("people/{path}")
    fun getPerson(@Path("path") path: String): Observable<Person>
}

