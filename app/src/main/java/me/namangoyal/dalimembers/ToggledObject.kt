package me.namangoyal.dalimembers

/**
 * Created by Naman
 * Class that has a string for the name of the view and boolean for if it is to be displayed
 * Also has an int referring to the ID of the view that will be displayed / not
 *
 * Has a companion object (static field) OBJECTS, an Array of the ToggledObjects
 */
data class ToggledObject (
        val id: Int,
        val displayString: String,
        var displayed: Boolean
) {
    companion object {
        /**
         * ToggledObject.OBJECTS is an Array of all the toggled objects to be displayed in the
         * navigation drawer in ProfileActivity
         */
        val OBJECTS:Array<ToggledObject>

        init {
            val picField = ToggledObject(R.id.profilePic,"Picture",true)
            val nameField = ToggledObject(R.id.profileName,"Name",true)
            val msgField = ToggledObject(R.id.profileMessage,"Message",false)
            val termsField = ToggledObject(R.id.profileTerms,"Terms",true)
            val projField = ToggledObject(R.id.profileProject,"Project",true)
            val linkField = ToggledObject(R.id.profileLink,"Link",false)
            val mapField = ToggledObject(R.id.profileMap,"Map",true)

            OBJECTS = arrayOf(picField,nameField,msgField,termsField,projField,linkField,mapField)
        }

    }
}