package me.namangoyal.dalimembers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

/**
 * Created by Naman
 * Fragment to hold a map view of all DALI members, and if you click on "location" from the list
 * view, it'll open directly to them
 */
class MapFragment : MapFragment(), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private lateinit var mMap: GoogleMap

    private lateinit var broadcastReceiver: BroadcastReceiver
    private val READY_ACTION = "com.cs65.gnf.lab4.ready"

    var zoomToID = -1 //stores the ID of the person to zoom to, if any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastReceiver = MyRecvr()
        getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0 //set our map
        mMap.setOnInfoWindowClickListener(this) //set info window click listener

        //Zoom out map
        mMap.moveCamera(CameraUpdateFactory.zoomTo(1f))

        //check if the list of DALI members is ready
        if ((activity as MainActivity).ready) { //if ready
            setMarkers()
            zoomToMember()
        }
        else { //if not ready
            val i = IntentFilter(READY_ACTION)

            LocalBroadcastManager.getInstance(activity)
                    .registerReceiver(broadcastReceiver,i) //register broadcast receiver
        }
    }

    /**
     * If an info window is double clicked, get that member's ID from the marker and start
     * ProfileActivity for that member
     */
    override fun onInfoWindowClick(p0: Marker) {
        val id = p0.tag as Int
        val member = (activity as MainActivity).members!![id]
        val baos = ByteArrayOutputStream()
        ObjectOutputStream(baos).writeObject(member) //write the member as a byte array

        val i = Intent(activity,ProfileActivity::class.java)
        i.putExtra("member",baos.toByteArray()) //put it in as an extra
        i.putExtra("id",id)

        startActivity(i) //start profile activity
    }

    /**
     * inner class that is a broadcast receiver, so that when the broadcast is received we can get
     * the list of members
     */
    inner class MyRecvr : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            setMarkers()
            zoomToMember()
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(MyRecvr())
        }
    }

    /**
     * Function that sets markers for all DALI members
     * only called after MainActivity has gotten the Array of members
     * and map has been created
     */
    private fun setMarkers() {
        val act = activity as MainActivity
        val members = act.members!!

        for (i in 0 until members.size) { //for each index in member
            val member = members[i]

            mMap.addMarker(MarkerOptions() //add a marker w/ member's position, name, and ID as a tag
                    .position(LatLng(member.lat_long[0],member.lat_long[1]))
                    .title(member.name))
                    .tag = i
        }
    }

    /**
     * Function to zoom to a specific DALI member.
     * Called after we have our list of DALI members, after the map is loaded
     * Called by the list Fragment, or by the Profile Activity
     */
    fun zoomToMember() {
        if (zoomToID!=-1) {
            val member = (activity as MainActivity).members!![zoomToID]
            mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(member.lat_long[0],member.lat_long[1])))
            mMap.moveCamera(CameraUpdateFactory.zoomTo(12f))
        }
    }

    /**
     * Changes the zoomToID and zooms to that member
     */
    fun zoomToMember(ID: Int) {
        zoomToID = ID
        zoomToMember()
    }

    fun zoomOut() {
        if (this::mMap.isInitialized) {
            mMap.moveCamera(CameraUpdateFactory.zoomTo(1f))
        }
    }
}
