package com.example.mysor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysor.listeners.OnLabelsListener
import com.example.mysor.server.DownloadImageBitmapTask
import com.example.mysor.server.GetLabels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnLabelsListener, View.OnClickListener
{

    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var imageView : ImageView
    private lateinit var mMap: GoogleMap
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapterPhotos
    private lateinit var labelInfo: View
    private lateinit var nameLabel: TextView
    private lateinit var descriptionLabel: TextView
    private lateinit var trashLevel: ImageView
    private lateinit var levelLabel: TextView
    private val pollutionIcon = intArrayOf(R.drawable.rubbish_1, R.drawable.rubbish_2,
        R.drawable.rubbish_3, R.drawable.rubbish_4, R.drawable.rubbish_5, R.drawable.rubbish_6,
        R.drawable.rubbish_7, R.drawable.rubbish_8, R.drawable.rubbish_9, R.drawable.rubbish_10)
    private var photos: ArrayList<Bitmap> = ArrayList<Bitmap>()


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        imageView = findViewById(R.id.imageView)
        nameLabel = findViewById(R.id.name_label)
        descriptionLabel = findViewById(R.id.description_label)
        trashLevel = findViewById(R.id.trash_level_info)
        levelLabel = findViewById(R.id.level_label)
        labelInfo = findViewById(R.id.scrollView2)
        recyclerView = findViewById(R.id.recyclerViewPhotos)
        val llm =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RVAdapterPhotos(this, photos)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = llm
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
        mMap.setOnMarkerClickListener { marker -> // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            //println("is")
            //adapter.addPhoto(BitmapFactory.decodeResource(resources,R.drawable.rubbish2))
            if (!labelInfo.isVisible) {
                labelInfo.visibility = View.VISIBLE
            }
            var label: Label
            label = marker.tag as Label
            descriptionLabel.text = label.getDescription()
            levelLabel.text = label.getLevel().toString()
            trashLevel.setImageResource(pollutionIcon[label.getLevel() - 1])
            var photos: ArrayList<Bitmap> = ArrayList<Bitmap>()
            var id = 0
            while (id < 10) { //
                var downloadImageBitmapTask = DownloadImageBitmapTask(this, label.getId(), id)
                downloadImageBitmapTask.execute()
                val image = downloadImageBitmapTask.get()
                photos.add(image)
                id++
            }
            adapter.setPhotos(photos)
//            val markerName = marker.id
//            Toast.makeText(
//                this@MapsActivity,
//                "Clicked location is $markerName",
//                Toast.LENGTH_SHORT
//            ).show()
            false
        }
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(52.737075164450516, 41.439688759798806)
        mMap.addMarker(MarkerOptions().position(sydney).title("Тамбов"))
            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rubbish2))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
        var getLabels = GetLabels(this, this)
        getLabels.execute()
    }

    override fun onLabelsCompleted(labels: ArrayList<Label>) {
        // var downloadImageBitmapTask = DownloadImageBitmapTask(this, 2)
        // downloadImageBitmapTask.execute()
        //val image = downloadImageBitmapTask.get()
        for (label in labels) {
            var coordinate = label.getCoordinates()
            var marker = mMap.addMarker(MarkerOptions().position(coordinate))
            marker.setIcon(BitmapDescriptorFactory.fromResource(pollutionIcon[label.getLevel() - 1]))
            //marker.setIcon(BitmapDescriptorFactory.fromBitmap(image))
            var data = label
            marker.tag = data
        }
    }

    override fun onLabelsError(error: String) {
        TODO("Not yet implemented")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            //R.id.addLabel -> Camera()
            R.id.addLabel -> AddProblem()
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

    fun AddProblem() {
        val intent = Intent(this, AddProblemActivity::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data !== null) {
                    var bitmap : Bitmap = data.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(bitmap)
                    /*
                    // Просто загрузка изображения
                    var downloadImage = DownloadImage(this, bitmap)
                    downloadImage.execute()
                     */
                    // Добавление метки
                    //var addLabel = AddLabel(this, bitmap)
                    //addLabel.execute()
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
//
//    override fun onMarkerClick(marker: Marker?): Boolean {
//        print("test")
//        return false
//    }
}