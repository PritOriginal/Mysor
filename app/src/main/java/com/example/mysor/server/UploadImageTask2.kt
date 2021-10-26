package com.example.mysor.server

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class UploadImageTask2(var mContext : Context): AsyncTask<Bitmap, Integer, Boolean>() {
    /** Upload file to this url  */
    private val UPLOAD_URL = mContext.resources.getString(com.example.mysor.R.string.urlImage)

    /** Send the file with this form name  */
    private val FIELD_FILE = "file"
    private val FIELD_LATITUDE = "latitude"
    private val FIELD_LONGITUDE = "longitude"

    /**
     * Prepare activity before upload
     */
    override fun onPreExecute() {
        super.onPreExecute()
        //setProgressBarIndeterminateVisibility(true)
        //mConfirm.setEnabled(false)
        //mCancel.setEnabled(false)
        //JColorChooser.showDialog(UPLOAD_PROGRESS_DIALOG)
    }

    /**
     * Clean app state after upload is completed
     */
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        //setProgressBarIndeterminateVisibility(false)
        //mConfirm.setEnabled(true)
       // mDialog.dismiss()
        if (result) {
            //JColorChooser.showDialog(UPLOAD_SUCCESS_DIALOG)
        } else {
            //JColorChooser.showDialog(UPLOAD_ERROR_DIALOG)
        }
    }

    override fun doInBackground(vararg image: Bitmap?): Boolean {
        return doFileUpload(image[0], UPLOAD_URL)
    }

    private fun doFileUpload(bitmap: Bitmap?, uploadUrl: String): Boolean {
        var uploadUrl: String? = uploadUrl
        var conn: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        val lineEnd = "\r\n"
        val twoHyphens = "--"
        val boundary = "*****"
        val separator = twoHyphens + boundary + lineEnd
        var bytesRead: Int
        var bytesAvailable: Int
        var bufferSize: Int
        val buffer: ByteArray
        val maxBufferSize = 1 * 1024 * 1024
        var sentBytes = 0

        //create a file to write bitmap data
        var file = File(mContext.cacheDir, "1")
        file.createNewFile()

        //Convert bitmap to byte array
        var bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
        var bitmapdata = bos.toByteArray();

        //write the bytes in file
        var fos: FileOutputStream? = null;
        try {
            fos = FileOutputStream(file);
        } catch (e: FileNotFoundException) {
            e.printStackTrace();
        }
        try {
            fos?.write(bitmapdata);
            fos?.flush();
            fos?.close();
        } catch (e: IOException) {
            e.printStackTrace();
        }

        val fileSize: Long = file.length()

        // The definitive url is of the kind:
        // http://host/report/latitude,longitude
        //* Пригодится
        //*uploadUrl += "/" + mLocation.getLatitude().toString() + "," + mLocation.getLongitude()

        // Send request
        try {
            // Configure connection
            val url = URL(uploadUrl)
            conn = url.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.doOutput = true
            conn.useCaches = false
            conn.requestMethod = "PUT"
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            //*publishProgress( 0)
            dos = DataOutputStream(conn.outputStream)

            // Send location params
            //*writeFormField(dos, separator, FIELD_LATITUDE, "" + mLocation.getLatitude())
            //*writeFormField(dos, separator, FIELD_LONGITUDE, "" + mLocation.getLongitude())

            // Send multipart headers
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes("Content-Disposition: form-data; name=\"" + FIELD_FILE + "\";filename=\""
                    + file.getName() + "\"" + lineEnd)
            dos.writeBytes(lineEnd)

            // Read file and create buffer
            val fileInputStream = FileInputStream(file)
            bytesAvailable = fileInputStream.available()
            bufferSize = Math.min(bytesAvailable, maxBufferSize)
            buffer = ByteArray(bufferSize)

            // Send file data
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            while (bytesRead > 0) {
                // Write buffer to socket
                dos.write(buffer, 0, bufferSize)

                // Update progress dialog
                sentBytes += bufferSize
                //publishProgress((sentBytes * 100 / fileSize).toInt())
                bytesAvailable = fileInputStream.available()
                bufferSize = Math.min(bytesAvailable, maxBufferSize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize)
            }

            // send multipart form data necesssary after file data
            dos.writeBytes(lineEnd)
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            dos.flush()
            dos.close()
            fileInputStream.close()
        } catch (ioe: IOException) {
            Log.e(ContentValues.TAG, "Cannot upload file: " + ioe.message, ioe)
            return false
        }

        // Read response
        return try {
            val responseCode: Int = conn.responseCode
            responseCode == 200
        } catch (ioex: IOException) {
            Log.e(ContentValues.TAG, "Upload file failed: " + ioex.message, ioex)
            false
        } catch (e: Exception) {
            Log.e(ContentValues.TAG, "Upload file failed: " + e.message, e)
            false
        }
    }
    @Throws(IOException::class)
    private fun writeFormField(dos: DataOutputStream?, separator: String, fieldName: String, fieldValue: String) {
        dos?.writeBytes(separator)
        dos?.writeBytes("Content-Disposition: form-data; name=\"$fieldName\"\r\n")
        dos?.writeBytes("\r\n")
        dos?.writeBytes(fieldValue)
        dos?.writeBytes("\r\n")
    }
}