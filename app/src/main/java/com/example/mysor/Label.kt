package com.example.mysor

import com.google.android.gms.maps.model.LatLng

class Label (private var id : Int, private var coordinates : LatLng, private var description: String, private var type: String) {

    fun setCoordinates(coordinates: LatLng) {
        this.coordinates = coordinates
    }
    fun getCoordinates(): LatLng {
        return coordinates
    }
    fun setDescription(description: String) {
        this.description = description
    }
    fun getDescription(): String {
        return description
    }
    fun setType(type: String) {
        this.type
    }
}