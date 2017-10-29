package br.com.tairoroberto.starwars.details.principal

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.base.Constants
import br.com.tairoroberto.starwars.model.Person

class DetailsPersonActivity : AppCompatActivity(), View.OnClickListener {

    private var person: Person? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_details_person)

        this.init()
        this.setupPersonData()

    }

    /**
     * Metodo inicial da activity
     */
    private fun init() {
        person = intent.getParcelableExtra<Person>(Constants.PERSON_EXTRA)
        supportActionBar?.title = person?.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val viewPager = this.findViewById<View>(R.id.activity_details_person_view_pager) as ViewPager
        viewPager.adapter = FragmentFilmsAdapter(supportFragmentManager)

        val rlMovies = this.findViewById<View>(R.id.activity_details_person_rl_movies) as RelativeLayout
        rlMovies.setOnClickListener(this)

        val rlInfo = this.findViewById<View>(R.id.activity_details_person_rl_info) as RelativeLayout
        rlInfo.setOnClickListener(this)

        val link = this.findViewById<View>(R.id.activity_details_person_link) as TextView
        link.setOnClickListener(this)

    }

    /**
     * Popula o personagem selecionado para exibicao na tela
     */
    private fun setupPersonData() {

        val name = this.findViewById<View>(R.id.activity_details_person_name) as TextView
        val height = this.findViewById<View>(R.id.activity_details_person_height) as TextView
        val mass = this.findViewById<View>(R.id.activity_details_person_mass) as TextView
        val hairColor = this.findViewById<View>(R.id.activity_details_person_hair_color) as TextView
        val skinColor = this.findViewById<View>(R.id.activity_details_person_skin_color) as TextView
        val eyeColor = this.findViewById<View>(R.id.activity_details_person_eye_color) as TextView
        val birthYear = this.findViewById<View>(R.id.activity_details_person_birth_year) as TextView
        val gender = this.findViewById<View>(R.id.activity_details_person_gender) as TextView
        val dateCapture = this.findViewById<View>(R.id.activity_details_person_date_capture) as TextView

        name.text = person?.name
        height.text = person?.height
        mass.text = person?.mass
        hairColor.text = person?.hairColor
        skinColor.text = person?.skinColor
        eyeColor.text = person?.eyeColor
        birthYear.text = person?.birthYear
        gender.text = person?.gender
        dateCapture.text = person?.dateCapture

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_top, android.support.v7.appcompat.R.anim.abc_slide_out_bottom)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_top, android.support.v7.appcompat.R.anim.abc_slide_out_bottom)
    }

    override fun onClick(view: View) {
        val rlPageFilms = this.findViewById<View>(R.id.activity_details_person_page_films) as RelativeLayout
        val llPageFilms = this.findViewById<View>(R.id.activity_details_person_ll_info) as LinearLayout
        val infoArrow = this.findViewById<View>(R.id.activity_details_person_info_arrow) as ImageView
        val moviesArrow = this.findViewById<View>(R.id.activity_details_person_arrow) as ImageView

        if (view.id == R.id.activity_details_person_rl_movies) {

            if (rlPageFilms.visibility == RelativeLayout.GONE) {
                rlPageFilms.visibility = RelativeLayout.VISIBLE
                llPageFilms.visibility = LinearLayout.GONE
                moviesArrow.setImageDrawable(this.getDrawable(R.drawable.ic_keyboard_arrow_up_white_48dp))
            } else {
                rlPageFilms.visibility = RelativeLayout.GONE
                moviesArrow.setImageDrawable(this.getDrawable(R.drawable.ic_keyboard_arrow_down_white_48dp))
            }
        } else if (view.id == R.id.activity_details_person_rl_info) {

            if (llPageFilms.visibility == LinearLayout.GONE) {
                rlPageFilms.visibility = RelativeLayout.GONE
                llPageFilms.visibility = LinearLayout.VISIBLE
                infoArrow.setImageDrawable(this.getDrawable(R.drawable.ic_keyboard_arrow_up_white_48dp))
            } else {
                llPageFilms.visibility = RelativeLayout.GONE
                infoArrow.setImageDrawable(this.getDrawable(R.drawable.ic_keyboard_arrow_down_white_48dp))
            }
        } else if (view.id == R.id.activity_details_person_link) {

            this.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + person?.latitude +
                    "," + person?.longitude + "(QR Code '" + person?.name + "') ")))
        }

    }

    /**
     * Subclasse do frangmento do PageView
     */
    private inner class FragmentFilmsAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return FilmsFragment(person, position)
        }

        override fun getCount(): Int {
            return person?.films?.size as Int
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}
