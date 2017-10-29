package br.com.tairoroberto.starwars.principal

import br.com.tairoroberto.starwars.model.Person
import br.com.tairoroberto.starwars.principal.adapter.PersonAdapter

interface IPrincipalPresenter {

    fun processCaptureQRCode(url: String)

    fun loadPersonAdapter(): PersonAdapter?

    fun sendDetailActivity(person: Person)

    fun sizeAdapterPersons(): Int?
}
