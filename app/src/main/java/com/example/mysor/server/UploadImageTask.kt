/*package com.example.mysor.server

import android.R
import android.content.ContentValues.TAG
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.swing.JColorChooser.showDialog


class UploadImageTask(var mContext : Context): AsyncTask<File, Integer, Boolean>() {
    /
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
        setProgressBarIndeterminateVisibility(true)
        mConfirm.setEnabled(false)
        mCancel.setEnabled(false)
        JColorChooser.showDialog(UPLOAD_PROGRESS_DIALOG)
    }

    /**
     * Clean app state after upload is completed
     */
    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        setProgressBarIndeterminateVisibility(false)
        mConfirm.setEnabled(true)
        mDialog.dismiss()
        if (result) {
            JColorChooser.showDialog(UPLOAD_SUCCESS_DIALOG)
        } else {
            JColorChooser.showDialog(UPLOAD_ERROR_DIALOG)
        }
    }

    override fun doInBackground(vararg image: File): Boolean? {
        return doFileUpload(image[0], UPLOAD_URL)
    }

    private fun onProgressUpdate(vararg values: Int) {
        super.onProgressUpdate(values)
        if (values[0] == 0) {
            mDialog.setTitle(getString(R.string.progress_dialog_title_uploading))
        }
        mDialog.setProgress(values[0])
    }

    /**
     * Upload given file to given url, using raw socket
     * @see https://stackoverflow.com/questions/4966910/androidhow-to-upload-mp3-file-to-http-server
     *
     *
     * @param file The file to upload
     * @param uploadUrl The uri the file is to be uploaded
     *
     * @return boolean true is the upload succeeded
     */
    private fun doFileUpload(file: File, uploadUrl: String): Boolean {
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
        val fileSize: Long = file.length()

        // The definitive url is of the kind:
        // http://host/report/latitude,longitude
        uploadUrl += "/" + mLocation.getLatitude().toString() + "," + mLocation.getLongitude()

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
            publishProgress(0)
            dos = DataOutputStream(conn.outputStream)

            // Send location params
            writeFormField(dos, separator, FIELD_LATITUDE, "" + mLocation.getLatitude())
            writeFormField(dos, separator, FIELD_LONGITUDE, "" + mLocation.getLongitude())

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
                publishProgress((sentBytes * 100 / fileSize).toInt())
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
            Log.e(TAG, "Cannot upload file: " + ioe.getMessage(), ioe)
            return false
        }

        // Read response
        return try {
            val responseCode: Int = conn.responseCode
            responseCode == 200
        } catch (ioex: IOException) {
            Log.e(TAG, "Upload file failed: " + ioex.getMessage(), ioex)
            false
        } catch (e: Exception) {
            Log.e(TAG, "Upload file failed: " + e.message, e)
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

 */