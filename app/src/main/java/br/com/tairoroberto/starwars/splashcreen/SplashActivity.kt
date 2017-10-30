package br.com.tairoroberto.starwars.splashcreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.principal.HomeActivity


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity, HomeActivity::class.java))

            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()

        }, 3000)
    }
}
