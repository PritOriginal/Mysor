package com.example.mysor.server

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.example.mysor.GPSTracker
import com.example.mysor.R
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class AddLabel(var mContext: Context, var bitmap: Bitmap, var description: String, var type: String): AsyncTask<URL, Integer, Boolean>() {
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doInBackground(vararg p0: URL?): Boolean {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val ImageBase: String = Base64.encodeToString(byteArray, Base64.DEFAULT)

        val gps = GPSTracker(mContext)
        if (gps.canGetLocation()) {
            latitude = gps.latitude
            longitude = gps.longitude
        }
        val coordinates = "$latitude,$longitude"
        var params = HashMap<String, String>()
        params["REQUEST"] = "addLabels"
        params["COORDINATES"] = coordinates
        params["DESCRIPTION"] = description
        params["TYPE"] = type
        params["IMAGE"] = ImageBase
        var data: String? = null

        val sbParams = StringBuilder()
        var i = 0
        for (key in params.keys) {
            try {
                if (i != 0) {
                    sbParams.append("&")
                }
                sbParams.append(key).append("=")
                        .append(URLEncoder.encode(params[key], "UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            i++
        }
        try {
            val res: Resources = mContext.resources
            val url = res.getString(R.string.url)
            val urlObj = URL(url)
            val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
            try {
                val postDataBytes =
                        sbParams.toString().toByteArray(charset("UTF-8"))
                print(sbParams)
                conn.apply {
                    setRequestProperty("Accept", "application/text")
                    setRequestProperty("Content-Length", postDataBytes.size.toString())
                    requestMethod = "POST"
                    connectTimeout = 5000
                    useCaches = false
                    doOutput = true
                    outputStream.write(postDataBytes)
                    connect()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                val in_: InputStream = BufferedInputStream(conn.inputStream)
                val reader1 = BufferedReader(InputStreamReader(in_))
                val result1 = StringBuilder()
                var line = reader1.readLine()
                while (line != null) {
                    result1.append("""
                        $line
                        
                        """.trimIndent())
                    line = reader1.readLine()
                }
                in_.close()
                val result = result1.toString()
                println("From server: $result")
            } finally {
                conn?.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        super.onPostExecute(result)
    }
}