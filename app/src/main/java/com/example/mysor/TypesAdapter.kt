package com.example.mysor

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView


class TypesAdapter(context: Context, resource: Int,var types: List<String>) : ArrayAdapter<String>(context, resource, types) {
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup) : View {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val row: View = inflater.inflate(R.layout.spinner_dropdown_item, parent, false)
        val label = row.findViewById<View>(R.id.weekofday) as TextView
        label.text = types[position]

        val icon: ImageView = row.findViewById<View>(R.id.type_) as ImageView

        if (dayOfWeek.get(position) === "Котопятница"
                || dayOfWeek.get(position) === "Субкота") {
            icon.setImageResource(R.drawable.paw_on)
        } else {
            icon.setImageResource(R.drawable.ic_launcher)
        }
        return row
    }
}