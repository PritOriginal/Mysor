package com.example.mysor

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AddProblemActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_problem)
        spinner = findViewById(R.id.spinner)
        var typesAdapter = TypesAdapter(this, icons, types)
        spinner.adapter = typesAdapter
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
    }
}
