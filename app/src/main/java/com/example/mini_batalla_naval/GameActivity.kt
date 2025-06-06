package com.example.mini_batalla_naval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.mini_batalla_naval.model.UpdaterTextView
import com.example.mini_batalla_naval.model.Tablero
import com.example.mini_batalla_naval.model.WinEventListener
import android.content.Intent
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu

//Grupo1: Kruk, Ivana y Rodriguez, Miguel
class GameActivity : AppCompatActivity(), WinEventListener {
    private lateinit var glTablero: GridLayout
    private lateinit var tableroLogico: Tablero
    private lateinit var updater: UpdaterTextView
    private lateinit var tvRestantes: TextView
    private lateinit var tvMovimientos: TextView
    private lateinit var tvAciertos: TextView
    private lateinit var tvMensajeJuego: TextView
    private var juegoTerminado: Boolean = false
    private var dimensionRecibida: Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //tablero y celdas
        dimensionRecibida = intent.getIntExtra("DIMENSION_TABLERO", 6)
        this.glTablero = findViewById<GridLayout>(R.id.glTablero)
        this.tableroLogico = Tablero(dimensionRecibida, dimensionRecibida)

        //text views
        this.tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        this.tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        this.tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        this.tvMensajeJuego = findViewById<TextView>(R.id.tvMensajeJuego)
        this.updater = UpdaterTextView(
            this,
            tvRestantes,
            tvMovimientos,
            tvAciertos,
            tvMensajeJuego,
            this.tableroLogico.getCantidadBarcos(),
            this
        )
        crearBotonesDelTablero(dimensionRecibida, dimensionRecibida)

        //botón de reinicio
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        crearBotonReiniciar(btnReiniciar)

        //
        val btnShowPopup: ImageButton = findViewById(R.id.btnMainHelp)
        btnShowPopup.setOnClickListener { it ->
            showGamePopupMenu(it)
        }


    }

    private fun crearBotonReiniciar(boton: Button) {
        boton.text = this.getString(R.string.menu_reiniciar)
        boton.setOnClickListener {
            //recreate()
            //reinicio de variables y objetos.
            //el objetiivo es evitar parpadeos porque recreate recorre t0do el ciclo de vida de la actividad.
            this.tableroLogico = Tablero(dimensionRecibida, dimensionRecibida)
            this.updater = UpdaterTextView(
                this,
                this.tvRestantes,
                this.tvMovimientos,
                this.tvAciertos,
                this.tvMensajeJuego,
                this.tableroLogico.getCantidadBarcos(),
                this
            )
            crearBotonesDelTablero(dimensionRecibida, dimensionRecibida)
        }
    }

    private fun crearBotonesDelTablero(
        filas: Int,
        columnas: Int,
    ) {
        //esta función crea botones con event listener que invocan internamente al updater 1 por botón 1 sola vez
        this.glTablero.removeAllViews()
        this.glTablero.rowCount = filas
        this.glTablero.columnCount = columnas
        val emojiAgua = this.getString(R.string.emoji_agua)
        val emojiBarco = this.getString(R.string.emoji_barco)
        val tamanioEmoji = when (filas) {
            6 -> 24
            8 -> 20
            10 -> 16
            else -> 24
        }


        //contenido del glTablero(gridlayout).
        for (i in 0 until filas) {
            for (j in 0 until columnas) {
                //crear botón y agregar a gridlayout. el estilo viene de styles.xml
                val boton = Button(this, null, 0, R.style.estilo_boton_tablero)
                boton.text = ""
                boton.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }

                boton.textSize = tamanioEmoji.toFloat()
                boton.setOnClickListener {
                    //control de ejecución de evento. Esto impide que se ejecute más de una vez.
                    if (this.juegoTerminado || !it.isEnabled) return@setOnClickListener

                    val fueAcierto = this.tableroLogico.celdaOcupada(i, j)
                    boton.text = if (fueAcierto) emojiBarco else emojiAgua
                    updater.registrarActividad(fueAcierto)
                    it.isEnabled = false
                }
                this.glTablero.addView(boton)
            }
        }
    }

    private fun deshabilitarTablero() {
        for (i in 0 until this.glTablero.childCount) {
            val boton = this.glTablero.getChildAt(i)
            if (boton.isEnabled) boton.isEnabled = false
        }
    }

    override fun onGameWon() {
        this.juegoTerminado = true
        deshabilitarTablero()
    }

    private fun showGamePopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_popup, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opInicio -> {
                    // Acción para "Inicio": Volver a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.opAyuda -> {
                    // Acción para "Ayuda": Abrir HelpActivity
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
