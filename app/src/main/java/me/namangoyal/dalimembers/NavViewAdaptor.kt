package me.namangoyal.dalimembers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Naman
 * An Adapter for the Nav View in the Profile Activity
 * Takes no params as it just uses ToggledObject.OBJECTS
 */
class NavViewAdaptor(private val context: Context): BaseAdapter() {
    override fun getCount(): Int = ToggledObject.OBJECTS.size

    override fun getItem(position: Int): Any = ToggledObject.OBJECTS[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row: View
        val holder: ViewHolder

        if (convertView==null) { //if the row has nothing rn
            val inflater = LayoutInflater.from(context)
            row = inflater.inflate(R.layout.row_navigation,parent,false)
            holder = ViewHolder(row) //set it to this holder
            row.tag = holder //set its tag to this holder
        }
        else { //if the row has something rn
            row = convertView
            holder = row.tag as ViewHolder //get holder from its tag
        }

        val obj = getItem(position) as ToggledObject

        holder.name.text = obj.displayString
        holder.img.setImageResource(if (obj.displayed) android.R.drawable.button_onoff_indicator_on
                                    else android.R.drawable.button_onoff_indicator_off)

        return row
    }

    inner class ViewHolder(v: View) {
        val name: TextView = v.findViewById(R.id.toggleText)
        val img: ImageView = v.findViewById(R.id.toggleImage)
    }
}