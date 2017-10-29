package br.com.tairoroberto.starwars.principal

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.base.*
import br.com.tairoroberto.starwars.base.Constants
import br.com.tairoroberto.starwars.model.Person
import br.com.tairoroberto.starwars.details.principal.DetailsPersonActivity
import br.com.tairoroberto.starwars.principal.adapter.PersonAdapter
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

class PrincipalPresenterImpl(private val mView: HomeActivity) : IPrincipalPresenter {
    private var personAdapter: PersonAdapter? = null

    /**
     * Processa o QRCode selecionado.
     *
     * @param url
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun processCaptureQRCode(url: String) {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        val path = url.replace("${mView.getContext().URL_BASE}people/", "").replace("/", "")

        mView.getContext().getRetrofit(mView.getContext())
                ?.getPerson(path)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ person ->

                    val location = mView.getLocationService()

                    if (location != null) {
                        person.latitude = location.latitude
                        person.longitude = location.longitude
                    }
                    person.url = url
                    person.userCapture = mView.getContext().getUserName()
                    person.dateCapture = sdf.format(Date())

                    this.savePerson(person)

                }, { error ->
                    Log.i("LOG", "${error.message}")
                })
    }

    /**
     * Carrega a lista de personagem do Shared preferences
     *
     * @return
     */
    override fun loadPersonAdapter(): PersonAdapter? {

        val preferences = mView.getContext().getPreferences()
        val persons = preferences.getStringSet(Constants.LIST_PERSONS, HashSet())

        personAdapter = PersonAdapter(persons, mView)
        personAdapter?.notifyDataSetChanged()

        return personAdapter
    }

    override fun sizeAdapterPersons(): Int? {
        return personAdapter?.count
    }


    /**
     * encaminha para Acitivity de delhes o personagem selecionado
     *
     * @param person
     */
    override fun sendDetailActivity(person: Person) {

        val intent = Intent(mView, DetailsPersonActivity::class.java)
        intent.action = Intent.ACTION_DEFAULT
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(Constants.PERSON_EXTRA, person)

        mView.startActivity(intent)
    }

    /**
     * Salva o Json capturado no SharedPrefereces local
     *
     * @param personLoad
     */
    private fun savePerson(personLoad: Person) {

        val gson = Gson()

        val preferences = mView.getContext().getPreferences()
        val persons = preferences.getStringSet(Constants.LIST_PERSONS, HashSet())
        for (person in persons) {
            val personParse = gson.fromJson(person, Person::class.java)
            if (personParse.name == personLoad.name) {
                mView.showToastMsg(mView.getString(R.string.activity_principal_erro_exists, personLoad.name?.toUpperCase()))
                return
            }
        }

        val editor = mView.getContext().getSharedEditor()
        editor.remove(Constants.LIST_PERSONS)
        editor.commit()

        persons.add(gson.toJson(personLoad))
        editor.putStringSet(Constants.LIST_PERSONS, persons)
        editor.commit()

    }
}
