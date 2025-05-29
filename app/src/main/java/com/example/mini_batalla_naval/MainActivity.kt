package com.example.mini_batalla_naval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.mini_batalla_naval.model.UpdaterTextView
import com.example.mini_batalla_naval.model.Tablero
import com.example.mini_batalla_naval.model.WinEventListener

//Grupo1: Kruk, Ivana y Rodriguez, Miguel
class MainActivity : AppCompatActivity(), WinEventListener {
    private lateinit var glTablero: GridLayout
    private lateinit var tableroLogico: Tablero
    private lateinit var updater: UpdaterTextView
    private var juegoTerminado: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //titulo
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        tvTitulo.text = this.getString(R.string.ui_titulo)

        //tablero y celdas
        glTablero = findViewById<GridLayout>(R.id.glTablero)
        val filas = 6
        val columnas = 6
        tableroLogico = Tablero(filas, columnas)

        //text views
        val tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        val tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        val tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        val tvMensajeJuego = findViewById<TextView>(R.id.tvMensajeJuego)
        updater = UpdaterTextView(this, tvRestantes, tvMovimientos, tvAciertos,tvMensajeJuego, tableroLogico.getCantidadBarcos(),this)
        crearBotonesDelTablero(filas, columnas)

        //botón de reinicio
        val bReiniciar = findViewById<Button>(R.id.bReiniciar)
        crearBotonReiniciar(bReiniciar)

    }

    private fun crearBotonReiniciar(boton: Button) {
        boton.text = this.getString(R.string.ui_reiniciar)
        boton.setOnClickListener{
            recreate()
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

        //contenido del glTablero(gridlayout).
        for (i in 0 until filas){
            for (j in 0 until columnas){
                //crear botón y agregar a gridlayout. el estilo viene de styles.xml
                val boton = Button(this, null, 0, R.style.estilo_boton_tablero)
                boton.text = ""
                boton.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }

                boton.setOnClickListener{
                    //control de ejecución de evento. Esto impide que se ejecute más de una vez.
                    if (this.juegoTerminado || !it.isEnabled) return@setOnClickListener

                    val fueAcierto = this.tableroLogico.celdaOcupada(i,j)
                    boton.text = if (fueAcierto) emojiBarco else emojiAgua
                    updater.registrarActividad(fueAcierto)
                    it.isEnabled = false
                }
                this.glTablero.addView(boton)
            }
        }
    }

    private fun deshabilitarTablero(){
        for (i in 0 until this.glTablero.childCount){
            val boton = this.glTablero.getChildAt(i)
            if (boton.isEnabled) boton.isEnabled = false
        }
    }

    override fun onGameWon() {
        this.juegoTerminado = true
        deshabilitarTablero()
    }
}





