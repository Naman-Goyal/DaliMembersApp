package me.namangoyal.dalimembers

import android.os.Bundle
import android.app.FragmentManager
import android.app.Fragment
import android.content.Intent
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.app.FragmentActivity
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.View
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.jetbrains.anko.longToast
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class MainActivity: FragmentActivity() {

    private val tabs: ArrayList<Fragment> = ArrayList()

    private val READY_ACTION = "com.cs65.gnf.lab4.ready"

    var members: Array<DaliMember>? = null

    private lateinit var mViewPager: ViewPager
    private lateinit var mAdapter: TabAdapter

    var ready = false //whether the list is ready

    override fun onCreate(savedInstanceState: Bundle?) {
        //Init
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign important vars
        val tabStrip: SlidingTabLayout = findViewById(R.id.tabs)
        mViewPager = findViewById(R.id.pager)
        mAdapter = TabAdapter(fragmentManager)

        //Add tabs
        tabs.add(MemberListFragment())
        tabs.add(MapFragment())

        //Assign adapter (see below)
        mViewPager.adapter = mAdapter

        //Set tab to evenly distribute, and then connect it to the viewPager
        tabStrip.setDistributeEvenly(true)
        tabStrip.setViewPager(mViewPager)

        //Now we'll get the member list so that it can be accessed by the fragments
        val listUrl = "http://mappy.dali.dartmouth.edu/members.json"
        Volley.newRequestQueue(this)
                .add(
                        object : StringRequest(Request.Method.GET, listUrl,
                                Response.Listener { response ->
                                    val gson = Gson()

                                    members = gson.fromJson(response,Array<DaliMember>::class.java)

                                    //put that this list is ready
                                    ready = true

                                    //then send a broadcast
                                    val intent = Intent()
                                    intent.action = READY_ACTION
                                    LocalBroadcastManager
                                            .getInstance(this)
                                            .sendBroadcast(intent)

                                    val toMapID = this.intent.getIntExtra("id",-1)
                                    Log.d("FLOW",toMapID.toString())

                                    if (toMapID!=-1) {
                                        Log.d("FLOW",toMapID.toString())
                                        (mAdapter.getItem(1) as MapFragment).zoomToID = toMapID
                                        mViewPager.currentItem = 1
                                        mAdapter.notifyDataSetChanged()
                                    }
                                },
                                Response.ErrorListener { error -> // Handle error cases
                                    when (error) {
                                        is NoConnectionError ->
                                            longToast("Connection Error")
                                        is TimeoutError ->
                                            longToast("Timeout Error")
                                        is AuthFailureError ->
                                            longToast("AuthFail Error")
                                        is NetworkError ->
                                            longToast("Network Error")
                                        is ParseError ->
                                            longToast("Parse Error")
                                        is ServerError ->
                                            longToast("Server Error")
                                        else -> longToast("Error: " + error)
                                    }
                                }
                        ) {
                            override fun setRetryPolicy(retryPolicy: RetryPolicy?): Request<*> {
                                return super.setRetryPolicy(DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                        2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
                            }
                        }
                )

        mViewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener(){
            //if we move to the List Fragment, zoom out in the map fragment
            override fun onPageSelected(position: Int) {
                if (position==0) (mAdapter.getItem(1) as MapFragment).zoomOut()
                super.onPageSelected(position)
            }
        })
    }

    override fun onDestroy() {
        ready = false
        Log.d("FLOW_IN_DESTROY",ready.toString())
        super.onDestroy()
    }

    /**
     * Adapter for the tab in this activity
     */
    inner class TabAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {

        override fun getCount(): Int  = tabs.size

        override fun getItem(position: Int): Fragment = tabs[position]

        // Two fields for tab
        override fun getPageTitle(position: Int): CharSequence? =
                when (position) {
                    0 -> "List"
                    1 -> "Map"
                    else -> null
                }
    }

    /**
     * Called by MemberListFragment
     * Calls openProfile(Int)
     */
    fun openProfile(v: View) {
        openProfile(v.tag as Int)
    }

    /**
     * Called by MapFragment
     * Opens ProfileActivity for that id
     */
    private fun openProfile(id: Int) {
        val baos = ByteArrayOutputStream()
        ObjectOutputStream(baos).writeObject(members!![id]) //get the member as a byte array

        val i = Intent(this,ProfileActivity::class.java)
        i.putExtra("member",baos.toByteArray()) //put it in as an extra
        i.putExtra("id",id)

        startActivity(i) //start the profile activity
    }

    /**
     * Called when user clicks on a member in the MemberListFragment
     * sets toMapID, a public var, to the member's ID and starts the map fragment
     */
    fun mapZoom(v: View) {

        //zoom in the map
        (mAdapter.getItem(1) as MapFragment).zoomToMember(v.tag as Int)

        //send to map frag
        mViewPager.currentItem = 1
        mAdapter.notifyDataSetChanged()
    }
}