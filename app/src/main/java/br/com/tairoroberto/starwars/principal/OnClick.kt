package br.com.tairoroberto.starwars.principal

import br.com.tairoroberto.starwars.model.Person

/**
 * Created by tairo on 10/29/17.
 */
interface OnClick {
    fun onItemClick(person: Person)
}