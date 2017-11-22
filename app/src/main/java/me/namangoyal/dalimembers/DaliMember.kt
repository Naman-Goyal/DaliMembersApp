package me.namangoyal.dalimembers

/**
 * Created by Naman
 *
 * Saves a dali member (used to get stuff from JSON, shown in the MemberListFragment)
 *
 * Kotlin Data classes are classes that autogenerate toString methods, getters and setters
 */
data class DaliMember (
        val name : String,
        val iconUrl : String,
        val url : String,
        val message : String,
        val lat_long : FloatArray,
        val terms_on : Array<String>,
        val project : Array<String>
) {
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