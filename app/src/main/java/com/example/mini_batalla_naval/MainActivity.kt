package com.example.mini_batalla_naval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import com.example.mini_batalla_naval.model.UpdaterTextView
import com.example.mini_batalla_naval.model.Tablero

//Grupo1: Kruk, Ivana y Rodriguez, Miguel
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //titulo
        val tvTitulo = findViewById<TextView>(R.id.tvTitulo)
        tvTitulo.text = this.getString(R.string.ui_titulo)

        //tablero y celdas
        val glTablero = findViewById<GridLayout>(R.id.glTablero)
        val filas = 6
        val columnas = 6
        val tablero = Tablero(filas, columnas)

        //text views
        val tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        val tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        val tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        val updater = UpdaterTextView(this, tvRestantes, tvMovimientos, tvAciertos, tablero.getCantidadBarcos())
        crearBotonesDelTablero(glTablero, tablero, filas, columnas, updater)

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
        glTablero: GridLayout,
        tablero: Tablero,
        filas: Int,
        columnas: Int,
        updater: UpdaterTextView
    ) {
        //esta función crea botones con event listener que invocan internamente al updater 1 por botón 1 sola vez
        glTablero.removeAllViews()
        glTablero.rowCount = filas
        glTablero.columnCount = columnas
        val toastTocado = this.getString(R.string.toast_tocado)
        val toastAgua = this.getString(R.string.toast_agua)
        val strTocado = this.getString(R.string.emoji_barco)//"🛳"
        val strAgua = this.getString(R.string.emoji_agua)//"🌊"

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
                    if (tablero.celdaOcupada(i,j)){
                        //tablero.celdaOcupada(fila,columna) retorna true si la celda está ocupada.
                        boton.text = strTocado
                        Toast.makeText(this, toastTocado, Toast.LENGTH_SHORT).show()
                        updater.incrementarAcierto()
                    } else {
                        boton.text = strAgua
                        Toast.makeText(this, toastAgua, Toast.LENGTH_SHORT).show()
                    }
                    updater.actualizar()
                    //se apaga el botón después de ser tocado.
                    boton.setEnabled(false)
                }
                glTablero.addView(boton)
            }
        }
    }
}





