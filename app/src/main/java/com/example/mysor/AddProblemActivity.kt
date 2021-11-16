package com.example.mysor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mysor.server.AddLabel
import com.example.mysor.server.DownloadImage

class AddProblemActivity : AppCompatActivity() {
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var spinner: Spinner
    private lateinit var imageView : ImageView
    private val icons = intArrayOf(R.drawable.rubbish, R.drawable.rubbish)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)
        imageView = findViewById(R.id.photo)
        spinner = findViewById(R.id.spinner)
        var types = resources.getStringArray(R.array.types).toList()
        var typesAdapter = TypesAdapter(this, icons, types)
        spinner.adapter = typesAdapter
        /*
        ArrayAdapter.createFromResource(
                this,
                R.array.types,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
         */
    }
    fun onClick(view: View?) {
        when (view?.id) {
            R.id.makePhoto -> Camera()
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
                    var bitmap : Bitmap = data.extras?.get("data") as Bitmap
                    imageView.setImageBitmap(bitmap)
                    // Просто загрузка изображения
                    var downloadImage = DownloadImage(this, bitmap)
                    downloadImage.execute()
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
