package br.com.tairoroberto.starwars.principal

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.model.Person

/**
 * Created by tairo on 10/29/17.
 */
class HomePersonAdapter(private val context: Context, private var listPerson: ArrayList<Person>, var onClick: OnClick) : RecyclerView.Adapter<HomePersonAdapter.ViewHolder>() {

    private var lastPosition = -1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = listPerson[position]

        holder.bind(context, person, onClick, position)
        holder.itemView.setOnClickListener({
            onClick.onItemClick(person)
        })
        setAnimation(holder.itemView, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.person_item, parent, false))
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position > 0) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int {
        return listPerson.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageView: ImageView = view.findViewById(R.id.imageView)
        private val textViewTitle: TextView = view.findViewById(R.id.textViewTitle)
        private val textViewUrl: TextView = view.findViewById(R.id.textViewUrl)

        fun bind(context: Context?, person: Person, onClick: OnClick, position: Int) {

            //imageView.loadImage(person.url)
            textViewTitle.text = person.name
            textViewUrl.text = person.url
        }
    }

    fun update(listPerson: ArrayList<Person>) {
        this.listPerson = listPerson
        this.notifyDataSetChanged()
    }
}