package com.example.mysor

class Label (private var coordinates : String, private var description: String, private var type: String) {
    fun setCoordinates(coordinates: String) {
        this.coordinates = coordinates
    }
    fun getCoordinates(): String {
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