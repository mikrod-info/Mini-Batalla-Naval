package com.example.mini_batalla_naval

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
//import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var btnJugar: Button
    private lateinit var btnHelp: ImageButton
    //private lateinit var etNombre: EditText
    private var dimensionTablero: Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        setupSpinner()

    }

    private fun setupSpinner() {
        spinner = this.findViewById<Spinner>(R.id.spinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dimensionTablero = when (position) {
                    0 -> 6
                    1 -> 8
                    2 -> 10
                    else -> 6
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                dimensionTablero = 6
            }
        }
    }

    private fun inicializarVistas() {
        btnJugar = this.findViewById<Button>(R.id.btnJugar)
        btnJugar.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java).apply {
                putExtra("DIMENSION_TABLERO", dimensionTablero)
            }
            startActivity(intent)
        }

        btnHelp = this.findViewById<ImageButton>(R.id.btnMainHelp)
        btnHelp.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }
    }
}