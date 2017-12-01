package me.namangoyal.dalimembers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

/**
 * Created by Naman as the view adapter for the list of members
 */
class MemberViewAdaptor(private val ctx: Context, private val memberArray: Array<DaliMember>) : BaseAdapter() {

    override fun getCount() : Int  = memberArray.size

    override fun getItem(position: Int): Any = memberArray[position]

    override fun getItemId(position: Int): Long = position.toLong()

    //Created a holder class — improves performance by reducing findView calls
    inner class ViewHolder(v : View) {
        val pic: ImageView = v.findViewById(R.id.memberPic)
        val name: TextView = v.findViewById(R.id.memberName)
        val message: TextView = v.findViewById(R.id.memberMessage)
        val mapButton: ImageView = v.findViewById(R.id.memberMapButton)
        val learnButton: TextView = v.findViewById(R.id.memberLearnMoreButton)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row: View
        val holder: ViewHolder

        if (convertView==null) { //if the row has nothing rn
            val inflater = LayoutInflater.from(ctx)
            row = inflater.inflate(R.layout.row_view,parent,false)
            holder = ViewHolder(row) //set it to this holder
            row.tag = holder //set its tag to this holder
        }
        else { //if the row has something rn
            row = convertView
            holder = row.tag as ViewHolder //get holder from its tag
        }

        //Get the member that is at this position
        val member = getItem(position) as DaliMember

        //Set all the fields in the view
        Picasso.with(ctx).load("http://mappy.dali.dartmouth.edu/"+member.iconUrl)
                .placeholder(R.drawable.stock_person).into(holder.pic)

        holder.name.text = member.name
        holder.message.text = member.message

        //buttons store important info in tags
        holder.learnButton.tag = memberArray.indexOf(member)
        holder.mapButton.tag = memberArray.indexOf(member)

        return row
    }
}