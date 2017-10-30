package br.com.tairoroberto.starwars.principal

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.base.*
import br.com.tairoroberto.starwars.model.Person
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by tairo on 10/29/17.
 */
class HomeModel(private val presenter: HomeContract.Preseter) : HomeContract.Model {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun getPerson(context: Context?, url: String) {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val path = url.replace("${context?.URL_BASE}people/", "").replace("/", "")

        context?.getRetrofit(context)
                ?.getPerson(path)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ person ->

                    val location = context.getLocationService()

                    if (location != null) {
                        person.latitude = location.latitude
                        person.longitude = location.longitude
                    }
                    person.url = url
                    person.userCapture = context.getUserName()
                    person.dateCapture = sdf.format(Date())

                    savePerson(presenter.getContext(), person)
                    loadPerson(presenter.getContext())

                }, { error ->
                    Log.i("LOG", "${error.message}")
                })
    }

    /**
     * Salva o Json capturado no SharedPrefereces local
     *
     * @param personLoad
     */
    override fun savePerson(context: Context?, personLoad: Person) {

        val gson = Gson()

        val preferences = context?.getPreferences()
        val persons = preferences?.getStringSet(Constants.LIST_PERSONS, HashSet())
        if (persons != null) {
            for (person in persons) {
                val personParse = gson.fromJson(person, Person::class.java)
                if (personParse.name == personLoad.name) {
                    presenter.showToastMsg(context.getString(R.string.exists, personLoad.name?.toUpperCase()))
                    return
                }
            }
        }

        val editor = context?.getSharedEditor()
        editor?.remove(Constants.LIST_PERSONS)
        editor?.commit()

        persons?.add(gson.toJson(personLoad))
        editor?.putStringSet(Constants.LIST_PERSONS, persons)
        editor?.commit()
    }

    override fun loadPerson(context: Context?) {
        val listPerson= ArrayList<Person>()
        val personString = context?.getPreferences()?.getStringSet(Constants.LIST_PERSONS, HashSet())

        personString?.forEach {
            val person = Gson().fromJson(it.toString(), Person::class.java)
            listPerson.add(person)
        }
        presenter.updateAdapter(listPerson)
    }
}