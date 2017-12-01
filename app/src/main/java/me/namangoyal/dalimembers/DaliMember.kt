package me.namangoyal.dalimembers

import java.io.Externalizable
import java.io.ObjectInput
import java.io.ObjectOutput

/**
 * Created by Naman
 *
 * Saves a dali member (used to get stuff from JSON, shown in the MemberListFragment)
 *
 * Kotlin Data classes are classes that autogenerate toString methods, getters and setters
 */
data class DaliMember(
        var name : String,
        var iconUrl : String,
        var url : String,
        var message : String,
        var lat_long : DoubleArray,
        var terms_on : Array<String>,
        var project : Array<String>
) : Externalizable {

    constructor():this("","","","",DoubleArray(2),Array<String>(0,{it.toString()}),Array<String>(0,{it.toString()}))

    override fun readExternal(input: ObjectInput) {
        name = input.readObject() as String
        iconUrl = input.readObject() as String
        url = input.readObject() as String
        message = input.readObject() as String
        lat_long = input.readObject() as DoubleArray
        terms_on = input.readObject() as Array<String>
        project = input.readObject() as Array<String>
    }

    override fun writeExternal(out: ObjectOutput) {
        out.writeObject(name)
        out.writeObject(iconUrl)
        out.writeObject(url)
        out.writeObject(message)
        out.writeObject(lat_long)
        out.writeObject(terms_on)
        out.writeObject(project)
    }

    override fun equals(other: Any?): Boolean {
        if (other==null) return false
        if (other !is DaliMember) return false

        if (name != other.name) return false
        if (iconUrl != other.iconUrl) return false
        if (url != other.url) return false
        if (message != other.message) return false
        if (!(other.lat_long contentEquals lat_long)) return false
        if (!(other.terms_on contentEquals terms_on)) return false
        if (!(other.project contentEquals project)) return false

        return true
    }

    override fun hashCode(): Int {
        var hash = 0

        hash += name.hashCode()
        hash += 13 * iconUrl.hashCode()
        hash += 17 * url.hashCode()
        hash += 19 * message.hashCode()
        hash += 23 * lat_long.hashCode()
        hash += 29 * terms_on.hashCode()
        hash += 31 * project.hashCode()

        return (hash%47)
    }
}