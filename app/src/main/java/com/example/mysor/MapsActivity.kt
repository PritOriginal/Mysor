package com.example.mysor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mysor.listeners.OnLabelsListener
import com.example.mysor.server.GetLabels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnLabelsListener {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
        var getLabels = GetLabels(this,this)
        getLabels.execute()
    }

    override fun onLabelsCompleted(labels: ArrayList<Label>) {
        for (label in labels) {
            var coordinate = label.getCoordinates()
            mMap.addMarker(MarkerOptions().position(coordinate))
                .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.rubbish))
        }
    }

    override fun onLabelsError(error: String) {
        TODO("Not yet implemented")
    }
}