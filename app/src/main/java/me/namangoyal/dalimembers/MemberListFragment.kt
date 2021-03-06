package me.namangoyal.dalimembers

import android.app.ListFragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Naman Goyal
 * Fragment that GETs and shows the list of DALI members
 */
class MemberListFragment : ListFragment() {

    private lateinit var broadcastReceiver: BroadcastReceiver
    private val READY_ACTION = "com.cs65.gnf.lab4.ready"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        broadcastReceiver = MyRecvr()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        //Inflate layout
        val rootView = inflater.inflate(R.layout.fragment_list,container,false) as ViewGroup

        if ((activity as MainActivity).ready) { //if ready
            getMembers()
        }
        else { //if not ready
            val i = IntentFilter(READY_ACTION)

            LocalBroadcastManager.getInstance(activity)
                    .registerReceiver(broadcastReceiver,i) //register broadcast receiver
        }

        return rootView
    }

    /**
     * inner class that is a broadcast receiver, so that when the broadcast is received we can get
     * the list of members
     */
    inner class MyRecvr : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            getMembers()
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(MyRecvr())
        }
    }

    /**
     * Called either at the beginning of onCreateView, or whenever the MainActivity sends a
     * broadcast signifying that the input file is ready
     */
    private fun getMembers() {
        val act = activity as MainActivity
        val members = act.members!!

        //Sort them into an array (here, by name)
        members.sortedBy {it.name}
        listAdapter = MemberViewAdaptor(context,members)

        retainInstance = true
    }
}