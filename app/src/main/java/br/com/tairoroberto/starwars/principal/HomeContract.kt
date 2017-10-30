package br.com.tairoroberto.starwars.principal

import android.content.Context
import br.com.tairoroberto.starwars.base.BasePresenter
import br.com.tairoroberto.starwars.base.BaseView
import br.com.tairoroberto.starwars.model.Person

class HomeContract {

    interface View : BaseView {
        fun showToastMsg(msg: String?)
        fun updateAdapter(listPerson: ArrayList<Person>)
    }

    interface Preseter : BasePresenter<View>{
        fun processCaptureQRCode(url: String)

        fun sendDetailActivity(person: Person)

        fun showToastMsg(string: String?)

        fun getContext(): Context?

        fun updateAdapter(listPerson: ArrayList<Person>)

        fun loadPerson()
    }

    interface Model {
        fun getPerson(context: Context?, url: String)

        fun savePerson(context: Context?, personLoad: Person)

        fun loadPerson(context: Context?)
    }
}
