package com.example.mysor

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class RVAdapterPhotos(var mContext: Context, var photos: ArrayList<Bitmap>) : RecyclerView.Adapter<RVAdapterPhotos.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //var cv: CardView
        var image: ImageView = itemView.findViewById(R.id.photo) as ImageView

        init {
            //cv = itemView.findViewById(R.id.cv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View =
            LayoutInflater.from(mContext).inflate(R.layout.photo_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.setImageBitmap(photos[position])
    }

    override fun getItemCount(): Int {
        return photos.size
    }

    public fun addPhoto(bitmap: Bitmap) {
        photos.add(bitmap)
        notifyDataSetChanged()
    }

}