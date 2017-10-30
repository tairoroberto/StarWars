package br.com.tairoroberto.starwars.details.principal

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.model.Person

class FilmsFragment : Fragment, View.OnClickListener {

    private var person: Person? = null
    private var position: Int? = null
    private var urlFilm: String? = null

    private val idImageFilm: Int
        @Throws(Exception::class)
        get() {

            val film = person?.films?.get(position as Int)
            val charIdFilm = film?.get(film.length - 2)

            when (charIdFilm) {
                '1' -> {
                    urlFilm = this.getString(R.string.film_4)
                    return R.drawable.film_4
                }
                '2' -> {
                    urlFilm = this.getString(R.string.film_5)
                    return R.drawable.film_5
                }
                '3' -> {
                    urlFilm = this.getString(R.string.film_6)
                    return R.drawable.film_6
                }
                '4' -> {
                    urlFilm = this.getString(R.string.film_1)
                    return R.drawable.film_1
                }
                '5' -> {
                    urlFilm = this.getString(R.string.film_2)
                    return R.drawable.film_2
                }
                '6' -> {
                    urlFilm = this.getString(R.string.film_3)
                    return R.drawable.film_3
                }
                '7' -> {
                    urlFilm = this.getString(R.string.film_7)
                    return R.drawable.film_7
                }
                '8' -> {
                    urlFilm = this.getString(R.string.film_8)
                    return R.drawable.film_8
                }
                else -> {
                    urlFilm = null
                    throw Exception(getString(R.string.not_found))
                }
            }
        }

    constructor()

    @SuppressLint("ValidFragment")
    constructor(person: Person?, position: Int) {
        this.person = person
        this.position = position
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_page_films, container, false) as ViewGroup
        this.init(view)
        return view
    }

    private fun init(view: ViewGroup) {
        val imageFilm = view.findViewById<View>(R.id.fragment_film) as ImageView
        try {
            imageFilm.setImageDrawable(ContextCompat.getDrawable(view.context, idImageFilm))

        } catch (e: Exception) {
            Log.e(FilmsFragment::class.java.name, e.message)
        }

        imageFilm.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == R.id.fragment_film) {
            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlFilm)))
            this.activity?.overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_top)
        }
    }
}
