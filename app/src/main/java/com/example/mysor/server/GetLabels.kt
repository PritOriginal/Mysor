package com.example.mysor.server

import android.os.AsyncTask
import com.example.mysor.Label
import com.example.mysor.listeners.OnLabelsListener
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


class GetLabels(var listener: OnLabelsListener) : AsyncTask<URL, Integer, ArrayList<Label>>() {
    lateinit var labels : ArrayList<Label>

    override fun doInBackground(vararg p0: URL?): ArrayList<Label> {
        var params = HashMap<String, String>()
        params.put("REQUEST", "getLabels")
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
            val url = "http://192.168.243.90/PythonProject/server_test.py"
            val urlObj = URL(url)
            val conn: HttpURLConnection = urlObj.openConnection() as HttpURLConnection
            try {
                val postDataBytes =
                    sbParams.toString().toByteArray(charset("UTF-8"))
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
                val `in`: InputStream = BufferedInputStream(conn.getInputStream())
                val reader1 = BufferedReader(InputStreamReader(`in`))
                val result1 = java.lang.StringBuilder()
                var line: String
                while (reader1.readLine().also { line = it } != null) {
                    result1.append(
                        """
                            $line
                            
                            """.trimIndent()
                    )
                }
                `in`.close()
                val result = result1.toString()
                println("From server: $result")
                val JObject = JSONObject(result)
                val jArray = JObject.getJSONArray("labels")
                i = 0
                while (i < jArray.length()) {
                    val jObject = jArray.getJSONObject(i)
                    val id = jObject.getInt("id")
                    val coordinates = jObject.getString("coordinates")
                    var x : Float
                    var y : Float
                    var s = StringBuilder()
                    for (c in coordinates) {
                        if (c != ',') {
                            s.append(c)
                        }
                        else if (x == 0f) {
                            x = s.toString().toFloat()
                            s.clear()
                        }
                    }
                    val LatCoordinates = LatLng(x, y))
                    val description = jObject.getString("description")
                    val type = jObject.getString("type")
                    val label = Label(id, LatCoordinates, description, type)
                    labels.add(label)
                    i++
                }
            } finally {
                conn?.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return labels
    }

    override fun onPostExecute(result: ArrayList<Label>?) {
        listener?.onLabelsCompleted(labels)
    }
}