package com.example.mini_batalla_naval

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.example.mini_batalla_naval.model.GameSettings
import com.example.mini_batalla_naval.model.GameSettings.getDimensionSegunOpcion

//Grupo1: Kruk, Ivana y Rodriguez, Miguel
class MainActivity : AppCompatActivity() {
    private lateinit var spinner: Spinner
    private lateinit var btnJugar: Button
    private lateinit var btnPopupMenu: ImageButton
    private lateinit var etNombre: EditText
    private var dimensionTablero: Int = 6
    private var nombreJugador: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inicializarVistas()
        setupButtonListener()
        setupSpinner()

    }

    private fun inicializarVistas() {
        this.etNombre = this.findViewById<EditText>(R.id.etNombre)
        this.btnJugar = this.findViewById<Button>(R.id.btnJugar)
        this.btnPopupMenu = this.findViewById<ImageButton>(R.id.btnPopupMenu)
        this.spinner = this.findViewById<Spinner>(R.id.spinner)
    }

    private fun setupButtonListener() {
        this.btnJugar.setOnClickListener {
            this.nombreJugador = etNombre.text.toString().trim()
            if (this.nombreJugador.isEmpty()) {
                this.etNombre.error = String.format(this.getString(R.string.input_error_empty))
                this.etNombre.requestFocus()
                return@setOnClickListener
            } else {
                val intent = Intent(this, GameActivity::class.java).apply {
                    putExtra("NOMBRE_JUGADOR", nombreJugador)
                    putExtra("DIMENSION_TABLERO", dimensionTablero)
                }
                startActivity(intent)
            }

        }

        this.btnPopupMenu.setOnClickListener {
            showMainPopupMenu(it)
        }
    }

    private fun setupSpinner() {
        this.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dimensionTablero = getDimensionSegunOpcion(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                dimensionTablero = GameSettings.DEFAULT_DIMENSION
            }
        }
    }

    private fun showMainPopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_pop_main, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opLeaderboard -> {
                    val intent = Intent(this, LeaderboardActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.opAyuda -> {
                    val intent = Intent(this, HelpActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }
}