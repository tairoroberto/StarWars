package br.com.tairoroberto.starwars.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Person(
        @SerializedName("name")
        var name: String? = null,

        @SerializedName("height")
        var height: String? = null,

        @SerializedName("mass")
        var mass: String? = null,

        @SerializedName("hair_color")
        var hairColor: String? = null,

        @SerializedName("skin_color")
        var skinColor: String? = null,

        @SerializedName("eye_color")
        var eyeColor: String? = null,

        @SerializedName("birth_year")
        var birthYear: String? = null,

        @SerializedName("gender")
        var gender: String? = null,

        @SerializedName("homeworld")
        var homeworld: String? = null,

        @SerializedName("films")
        var films: List<String>? = null,

        @SerializedName("species")
        var species: List<String>? = null,

        @SerializedName("vehicles")
        var vehicles: List<String>? = null,

        @SerializedName("starships")
        var starships: List<String>? = null,

        @SerializedName("created")
        var created: String? = null,

        @SerializedName("edited")
        var edited: String? = null,

        @SerializedName("url")
        var url: String? = null,

        var latitude: Double? = 0.toDouble(),

        var longitude: Double? = 0.toDouble(),

        var userCapture: String? = null,

        var dateCapture: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.createStringArrayList(),
            source.createStringArrayList(),
            source.createStringArrayList(),
            source.createStringArrayList(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readValue(Double::class.java.classLoader) as Double?,
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(height)
        writeString(mass)
        writeString(hairColor)
        writeString(skinColor)
        writeString(eyeColor)
        writeString(birthYear)
        writeString(gender)
        writeString(homeworld)
        writeStringList(films)
        writeStringList(species)
        writeStringList(vehicles)
        writeStringList(starships)
        writeString(created)
        writeString(edited)
        writeString(url)
        writeValue(latitude)
        writeValue(longitude)
        writeString(userCapture)
        writeString(dateCapture)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Person> = object : Parcelable.Creator<Person> {
            override fun createFromParcel(source: Parcel): Person = Person(source)
            override fun newArray(size: Int): Array<Person?> = arrayOfNulls(size)
        }
    }
}
