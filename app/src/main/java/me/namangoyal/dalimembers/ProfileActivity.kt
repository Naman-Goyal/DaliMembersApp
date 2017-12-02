package me.namangoyal.dalimembers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

/**
 * Created by Naman
 * Activity to display a profile
 */
class ProfileActivity: Activity(), AdapterView.OnItemClickListener {
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mListView: ListView

    private lateinit var member: DaliMember

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //Get the DALI member
        val bais = ByteArrayInputStream(intent.getByteArrayExtra("member"))
        member = ObjectInputStream(bais).readObject() as DaliMember

        //Set the fields that change per member
        Picasso.with(this) //picture
                .load("http://mappy.dali.dartmouth.edu/"+member.iconUrl)
                .placeholder(R.drawable.stock_person).into(findViewById<ImageView>(R.id.profilePic))
        (findViewById<TextView>(R.id.profileName)).text = member.name //name
        (findViewById<TextView>(R.id.profileMessage)).text = member.message //message
        (findViewById<TextView>(R.id.profileTerms2)).text = member.terms_on.joinToString { it } //terms
        (findViewById<TextView>(R.id.profileProject2)).text = if (member.project.isEmpty()) "Nothing right now" else member.project[0]

        (findViewById<TextView>(R.id.profileLink)).setOnClickListener { //profile link

            val uri = Uri.parse("http:"+member.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        (findViewById<ImageButton>(R.id.profileMapClick)).setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            val id = intent.getIntExtra("id",-1)
            i.putExtra("id",id)
            startActivity(i)
        }

        //Set those that should be invisible to that
        ToggledObject.OBJECTS
                .filter {!it.displayed}
                .forEach {it -> findViewById<View>(it.id).visibility = View.INVISIBLE}

        //Set up the Navigation Drawer
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mListView = findViewById(R.id.left_drawer)

        mListView.adapter = NavViewAdaptor(this)
        mListView.onItemClickListener = this
    }

    /**
     * On click, toggle visibility as well as the on/off icon in the navigation view
     */
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val obj = ToggledObject.OBJECTS[position]
        obj.displayed = !obj.displayed
        if (obj.displayed) { //if it's now displayed
            //set that in the view itself
            view?.findViewById<ImageView>(R.id.toggleImage)?.setImageResource(android.R.drawable.button_onoff_indicator_on)

            //change visibility in profileActivity
            findViewById<View>(obj.id).visibility = View.VISIBLE
        }
        else { //if it's not displayed
            //set that in the view
            view?.findViewById<ImageView>(R.id.toggleImage)?.setImageResource(android.R.drawable.button_onoff_indicator_off)

            //change visibility in profileActivity
            findViewById<View>(obj.id).visibility = View.INVISIBLE
        }
    }
}