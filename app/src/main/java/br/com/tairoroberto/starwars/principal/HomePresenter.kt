package br.com.tairoroberto.starwars.principal

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import br.com.tairoroberto.starwars.base.Constants
import br.com.tairoroberto.starwars.details.principal.DetailsPersonActivity
import br.com.tairoroberto.starwars.model.Person

class HomePresenter : HomeContract.Preseter {

    private var model: HomeContract.Model? = null
    private var view: HomeContract.View? = null

    override fun attachView(view: HomeContract.View) {
        this.model = HomeModel(this)
        this.view = view
    }

    override fun detachView() {
        this.view = null
    }

    /**
     * Processa o QRCode selecionado.
     *
     * @param url
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun processCaptureQRCode(url: String) {
        model?.getPerson(view?.getContext(), url)
    }

    override fun updateAdapter(listPerson: ArrayList<Person>) {
        view?.updateAdapter(listPerson)
    }

    /**
     * encaminha para Acitivity de delhes o personagem selecionado
     *
     * @param person
     */
    override fun sendDetailActivity(person: Person) {

        val intent = Intent(view?.getContext(), DetailsPersonActivity::class.java)
        intent.action = Intent.ACTION_DEFAULT
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(Constants.PERSON_EXTRA, person)

        view?.getContext()?.startActivity(intent)
    }

    override fun showToastMsg(string: String?) {
        view?.showToastMsg(string)
    }

    override fun getContext(): Context? {
        return view?.getContext()
    }

    override fun loadPerson() {
        model?.loadPerson(view?.getContext())
    }
}
