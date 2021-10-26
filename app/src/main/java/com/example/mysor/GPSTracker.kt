package com.example.mysor

import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log


class GPSTracker(mContext: Context): LocationListener {

    // The minimum distance to change Updates in meters
    private val MIN_UPDATE_DISTANCE: Float = 10f

    // The minimum time between updates in milliseconds
    private val MIN_UPDATE_TIME: Long = 1000 * 60

    private val mContext: Context? = null

    // Flag for GPS status
    var isGPSEnabled = false

    // Flag for network status
    var isNetworkEnabled = false

    // Flag for GPS status
    var canGetLocation = false
    private var locationManager: LocationManager? = null
    var latitude = 0.0
    var longitude= 0.0
    var location : Location? = null

        @SuppressLint("MissingPermission")
        get() {
            try {
                locationManager = mContext?.getSystemService(LOCATION_SERVICE) as LocationManager

                // Getting GPS status
                isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

                // Getting network status
                isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!isGPSEnabled && !isNetworkEnabled) {
                    // No network provider is enabled
                } else {
                    this.canGetLocation = true
                    if (isNetworkEnabled) {
                        locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_UPDATE_TIME,
                                MIN_UPDATE_DISTANCE, this)
                        Log.d("Network", "Network")
                        if (locationManager != null) {
                            location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                            if (location != null) {
                                latitude = location!!.latitude
                                longitude = location!!.longitude
                            }
                        }
                    }
                    // If GPS enabled, get latitude/longitude using GPS Services
                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager!!.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_UPDATE_TIME,
                                    MIN_UPDATE_DISTANCE, this)
                            Log.d("GPS Enabled", "GPS Enabled")
                            if (locationManager != null) {
                                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                                if (location != null) {
                                    latitude = location!!.latitude
                                    longitude = location!!.longitude
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return location
        }

    /**
     * Function to check GPS/Wi-Fi enabled
     * @return boolean
     */
    fun canGetLocation(): Boolean {
        return canGetLocation
    }


    override fun onLocationChanged(p0: Location?) {
        TODO("Not yet implemented")
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("Not yet implemented")
    }

}