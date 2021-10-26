package com.example.mysor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class TypesAdapter(val context: Context, var icons: IntArray, var types: List<String>) : BaseAdapter() {
    override fun getCount(): Int {
        return types.size
    }

    override fun getItem(i: Int): Any? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup?): View {
        val inflater = LayoutInflater.from(context)
        var view = inflater.inflate(R.layout.spinner_dropdown_item, null);
        var icon = view.findViewById<ImageView>(R.id.type_image)
        var type = view.findViewById<TextView>(R.id.type_label)
        icon.setImageResource(icons[i])
        type.text = types[i]
        return view
    }
