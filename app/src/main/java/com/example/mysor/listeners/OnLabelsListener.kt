package com.example.mysor.listeners

import com.example.mysor.Label

interface OnLabelsListener {
    fun onLabelsCompleted(labels : ArrayList<Label>)
    fun onLabelsError(error : String)
}