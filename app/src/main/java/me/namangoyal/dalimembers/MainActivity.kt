package me.namangoyal.dalimembers

import android.os.Bundle
import android.app.FragmentManager
import android.app.Fragment
import android.content.Intent
import android.preference.PreferenceManager
import android.support.v13.app.FragmentPagerAdapter
import android.support.v4.app.FragmentActivity
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.ViewPager
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.jetbrains.anko.longToast

class MainActivity: FragmentActivity() {

    private val tabs: ArrayList<Fragment> = ArrayList()

    private val READY_ACTION = "com.cs65.gnf.lab4.ready"

    var members: Array<DaliMember>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //Init
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign global vars
        val tabStrip: SlidingTabLayout = findViewById(R.id.tabs)
        val mViewPager: ViewPager = findViewById(R.id.pager)
        val adapter = TabAdapter(fragmentManager)

        //Add tabs
        tabs.add(MemberListFragment())
        tabs.add(ProfileFragment())
        tabs.add(MapFragment())

        //Assign adapter (see below)
        mViewPager.adapter = adapter

        //Set tab to evenly distribute, and then connect it to the viewPager
        tabStrip.setDistributeEvenly(true)
        tabStrip.setViewPager(mViewPager)

        //Now we'll get the member list so that it can be accessed by the fragments

        //First, make sure that the cat list has not already been saved to internal storage
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        //Make the URL
        val listUrl = "http://mappy.dali.dartmouth.edu/members.json"

        //Open volley req (end result of this is saving the cat list to internal storage)
        Volley.newRequestQueue(this)
                .add(
                        object : StringRequest(Request.Method.GET, listUrl,
                                Response.Listener { response ->
                                    val gson = Gson()

                                    members = gson.fromJson(response,Array<DaliMember>::class.java)

                                    //put that this list is ready
                                    prefs.edit()
                                            .putBoolean(READY_ACTION,true)
                                            .apply()

                                    //then send a broadcast
                                    val intent = Intent()
                                    intent.action = READY_ACTION
                                    LocalBroadcastManager
                                            .getInstance(this)
                                            .sendBroadcast(intent)
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
    }

    /**
     * Adapter for the tab in this activity
     */
    inner class TabAdapter (fm: FragmentManager): FragmentPagerAdapter(fm) {

        override fun getCount(): Int  = tabs.size

        override fun getItem(position: Int): Fragment = tabs[position]

        // Four fields for tab
        override fun getPageTitle(position: Int): CharSequence? =
                when (position) {
                    0 -> "List"
                    1 -> "Profile"
                    2 -> "Map"
                    else -> null
                }
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .clear()
                .apply()
        
        super.onDestroy()
    }
}