package com.example.mysor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class AddProblemActivity : AppCompatActivity() {
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var spinner: Spinner
    private lateinit var imageView : ImageView
    private lateinit var description: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapterPhotos
    private val icons = intArrayOf(R.drawable.rubbish, R.drawable.rubbish)
    private var photos: ArrayList<Bitmap> = ArrayList<Bitmap>()
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)
        description = findViewById(R.id.newDescriptionLabel)
        imageView = findViewById(R.id.photo)
        spinner = findViewById(R.id.spinner)
        recyclerView = findViewById(R.id.recyclerViewPhotos)
        val llm =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RVAdapterPhotos(this, photos)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = llm
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
                    //Log.e("Original   dimensions", bitmap.width.toString());
                //    recyclerView.adapter
                    //imageView.setImageBitmap(bitmap)
                    // Определение типа
                    lateinit var type: String
                    when (spinner.selectedItemPosition) {
                        0 -> type = "m"
                        1 -> type = "p"
                    }
                    // Добавление метки
                 //   var addLabel = AddLabel(this, bitmap, description.text.toString(), type)
                 //   addLabel.execute()
                    // Просто загрузка изображения
                 //   var downloadImage = DownloadImage(this, bitmap)
                 //   downloadImage.execute()
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
