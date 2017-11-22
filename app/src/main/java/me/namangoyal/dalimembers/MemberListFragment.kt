package me.namangoyal.dalimembers

import android.app.ListFragment
import android.os.Bundle

/**
 * Created by Naman Goyal
 * Fragment that GETs and shows the list of DALI members
 */
class MemberListFragment : ListFragment() {

    private var memberList: ArrayList<DaliMember>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        /**
         * get JSON info and put it into an array
         *
         */
        super.onCreate(savedInstanceState)
    }
}