package com.example.mysor

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysor.listeners.OnLabelsListener
import com.example.mysor.server.DownloadImage
import com.example.mysor.server.DownloadImageBitmapTask
import com.example.mysor.server.GetLabels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnLabelsListener, View.OnClickListener {

    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var imageView : ImageView

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        imageView = findViewById(R.id.imageView)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(52.737075164450516, 41.439688759798806)
        mMap.addMarker(MarkerOptions().position(sydney).title("Тамбов"))
            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rubbish))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
        var getLabels = GetLabels(this, this)
        getLabels.execute()
    }

    override fun onLabelsCompleted(labels: ArrayList<Label>) {
        var downloadImageBitmapTask = DownloadImageBitmapTask(this)
        downloadImageBitmapTask.execute()
        val image = downloadImageBitmapTask.get()
        for (label in labels) {
            var coordinate = label.getCoordinates()
            mMap.addMarker(MarkerOptions().position(coordinate))
                .setIcon(BitmapDescriptorFactory.fromBitmap(image))
        }
    }

    override fun onLabelsError(error: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.addLabel -> Camera()
        }
    }

    fun Camera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        } catch (e: ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data !== null) {
                    var bitmap = data.extras?.get("data")
                    imageView.setImageBitmap(bitmap as Bitmap)
                    var downloadImage = DownloadImage(this, bitmap)
                    downloadImage.execute()
                    /*
                    var bitmap = data.extras?.get("data")
                    imageView.setImageBitmap(bitmap as Bitmap)

                    //create a file to write bitmap data
                    var file = File(cacheDir, "1")
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
                    val reqFile: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val body: MultipartBody.Part = MultipartBody.Part.createFormData("upload", file.name, reqFile)

                    val service = Retrofit.Builder().baseUrl(resources.getString(R.string.urlImage)).build().create(Service::class.java)
                    val req: Call<ResponseBody?>? = service.postImage(body)
                    req?.enqueue(object : Callback<ResponseBody?> {
                        override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {
                            // Do Something with response
                        }

                        override fun onFailure(call: Call<ResponseBody?>?, t: Throwable) {
                            //failure message
                            t.printStackTrace()
                        }
                    })

                     */
                     */
                    //var uploadImageTask = UploadImageTask2(this)
                    // uploadImageTask.execute(bitmap as Bitmap?)
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}