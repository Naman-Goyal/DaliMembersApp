package me.namangoyal.dalimembers

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import java.io.ByteArrayInputStream
import java.io.ObjectInputStream

/**
 * Created by Naman
 * Activity to display a profile
 */
class ProfileActivity: Activity() {
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
        (findViewById<TextView>(R.id.profileProject2)).text = if (member.project.isEmpty()) "None" else member.project[0]

        (findViewById<TextView>(R.id.profileLink)).setOnClickListener { //profile link

            val uri = Uri.parse("https:"+member.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        (findViewById<ImageButton>(R.id.profileMapClick)).setOnClickListener {
            val i = Intent(this,MainActivity::class.java)
            val id = intent.getIntExtra("id",-1)
            i.putExtra("id",id)
            startActivity(i)
        }
    }
}