package com.example.mysor

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysor.server.AddLabel


class AddProblemActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {
    private val REQUEST_TAKE_PHOTO = 1
    private lateinit var toolbar: Toolbar
    private lateinit var spinner: Spinner
    private lateinit var raitingBar: RatingBar
    private lateinit var seekBar: SeekBar
    //private lateinit var imageView : ImageView
    private lateinit var description: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RVAdapterPhotos
    private val icons = intArrayOf(R.drawable.rubbish2, R.drawable.rubbish)
    private var photos: ArrayList<Bitmap> = ArrayList<Bitmap>()

    private lateinit var textTest: TextView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)
        //toolbar = findViewById(R.id.toolbar)
        //setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        description = findViewById(R.id.newDescriptionLabel)
        //imageView = findViewById(R.id.photo)
        spinner = findViewById(R.id.spinner)
        textTest = findViewById(R.id.textView3)
        seekBar = findViewById(R.id.seekBar)
        seekBar = findViewById(R.id.seekBar)
        seekBar.setOnSeekBarChangeListener(this)
        raitingBar = findViewById(R.id.ratingBar)
        raitingBar.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar, rating, fromUser -> //Присваиваем элементу TextView текстовое значение,
            //которое указано в скобках и добавляем значение из RatingBar:
            textTest.setText("Выбрано звезд: $rating")
        })
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
            R.id.createLabel -> addLabels()
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

    fun addLabels() {
        // Просто загрузка изображения
        //var downloadImage = DownloadImage(this, adapter.getPhotos())
        //downloadImage.execute()
        // Определение типа
        lateinit var type: String
        when (spinner.selectedItemPosition) {
            0 -> type = "m"
            1 -> type = "p"
        }
        // Добавление метки
           var addLabel = AddLabel(this, adapter.getPhotos(), description.text.toString(), type, raitingBar.rating)
           addLabel.execute()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_TAKE_PHOTO -> {
                if (resultCode == Activity.RESULT_OK && data !== null) {
                    var bitmap : Bitmap = data.extras?.get("data") as Bitmap
                    adapter.addPhoto(bitmap)
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
                }
            }
            else ->{
                Toast.makeText(this, "Wrong request code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        seekBar.progressDrawable.setColorFilter(Color.rgb(255,255 - seekBar.progress,0), PorterDuff.Mode.SRC_IN)
        textTest.text = (seekBar.progress / 28 + 1).toString()
        seekBar.thumb.setColorFilter(Color.rgb(255,255 - seekBar.progress,0), PorterDuff.Mode.SRC_IN);
        textTest.setTextColor(Color.rgb(255,255 - seekBar.progress,0))
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {

    }

    override fun onStopTrackingTouch(p0: SeekBar?) {

    }
}
