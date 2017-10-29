package br.com.tairoroberto.starwars.base

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by tairo on 10/29/17.
 */
fun Context.getPreferences(): SharedPreferences {
    return getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
}

fun Context.getSharedEditor(): SharedPreferences.Editor {

    val preferences = getSharedPreferences(Constants.MY_PREFERENCES, Context.MODE_PRIVATE)
    return preferences.edit()

}

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info: NetworkInfo? = cm.activeNetworkInfo

    return info != null
}


fun Context.getLocationService(): Location? {
    val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        return null
    }

    val listener = object : LocationListener {
        override fun onLocationChanged(location: Location) {

        }

        override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {

        }

        override fun onProviderEnabled(s: String) {

        }

        override fun onProviderDisabled(s: String) {

        }
    }

    lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 10f, listener)
    val location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
    lm.removeUpdates(listener)
    return location
}

fun Context.getUserName(): String? {
    val c = contentResolver.query(ContactsContract.Profile.CONTENT_URI, null, null, null, null)
    if (c != null) {
        c.moveToFirst()
        val name = c.getString(c.getColumnIndex("display_name"))
        c.close()
        return name
    }
    return null
}

val Context.URL_BASE: String
        get() = "https://swapi.co/api/"

fun Context.getRetrofit(context: Context): Api? {

    var retrofit: Retrofit? = null

    if (retrofit == null) {

        val client = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        val gson = GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .setLenient()
                .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                .create()


        retrofit = Retrofit.Builder()
                .baseUrl(context.URL_BASE)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
    }

    return retrofit?.create(Api::class.java)
}