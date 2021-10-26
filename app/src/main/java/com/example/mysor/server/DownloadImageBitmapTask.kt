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

class DownloadImageBitmapTask(var mContext : Context) : AsyncTask<String, Void, Bitmap>() {
    override fun doInBackground(vararg urls: String?): Bitmap? {
        val res: Resources = mContext.resources
        val url = res.getString(R.string.urlImage) + "/repair.png"
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