package com.example.mysor.server

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.widget.ImageView
import com.example.mysor.R
import java.io.InputStream
import java.net.URL

class DownloadImageBitmapTask(var mContext : Context, var image : Int, var id_label : Int, var id_photo : Int) : AsyncTask<String, Void, Bitmap>() {
    constructor(mContext: Context, image: Int) : this(mContext, image, 0, 0)
    constructor(mContext : Context, id_label : Int, id_photo : Int) : this(mContext, 0 , id_label, id_photo)

    override fun doInBackground(vararg urls: String?): Bitmap? {
        val res: Resources = mContext.resources
        var url = ""
        if (image != 0) {
            when (image) {
                1 -> url = res.getString(R.string.urlImage) + "/repair.png"
                2 -> url = res.getString(R.string.urlImage) + "/in.png"
            }
        } else {
            url = res.getString(R.string.urlImage) + "/l_" + id_label + "/new_image" + id_photo + ".png"
        }
        var urldisplay = url
        var mIcon11 : Bitmap? = null
        try {
            val in_: InputStream = URL(urldisplay).openStream()
            mIcon11 = BitmapFactory.decodeStream(in_)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mIcon11
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
    }
}