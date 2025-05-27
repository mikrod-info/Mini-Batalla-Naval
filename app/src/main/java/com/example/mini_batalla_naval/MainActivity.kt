package com.example.mini_batalla_naval

import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.mini_batalla_naval.model.CeldaClickListener
import com.example.mini_batalla_naval.model.Tablero
import androidx.core.content.ContextCompat
import android.content.Context
import android.widget.TextView
import com.example.mini_batalla_naval.model.UpdaterTextView

//Grupo1: Kruk, Ivanaa y Rodriguez, Miguel
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tablero y celdas
        val gridLayout = findViewById<GridLayout>(R.id.glTablero)
        val filas = 6
        val columnas = 6
        val tablero = Tablero(filas, columnas)
        //text views
        val barcosRestantes = findViewById<TextView>(R.id.tvRestantes)
        val movimientos = findViewById<TextView>(R.id.tvMovimientos)
        val aciertos = findViewById<TextView>(R.id.tvAciertos)
        val updater =
            UpdaterTextView(barcosRestantes, movimientos, aciertos, tablero.cantidadBarcos)
        //botones con event listener que invocan internamente al updater 1 por botón 1 sola vez
        crearBotonesDelTablero(
            this,
            gridLayout,
            tablero,
            filas,
            columnas,
            updater
        )
        //botón reinicio
        val bReiniciar = findViewById<Button>(R.id.bReiniciar)
        crearBotonReinicio(bReiniciar)

//para implementar: guardar y recuperar estado. soluciona giro horizontal solo crea 6 columnas. revisar si el guardar estado soluciona el problema
//        val estado = Estado(tablero, updater)
//        override fun onSaveInstanceState(outState: Bundle) {
//            super.onSaveInstanceState(outState)
//            outState.parciable(Estado.getUpdater, Estado.getTablero)
//        }
//
//        override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//            super.onRestoreInstanceState(savedInstanceState)
//            estado = savedInstanceState.(Estado.getUpdater, Estado.getTablero)
//        }

    }

    private fun crearBotonReinicio(boton: Button) {
        boton.text = "Reiniciar"
        boton.textSize = 22f
        boton.setTextColor(ContextCompat.getColor(this,R.color.white))
        boton.setBackgroundColor(ContextCompat.getColor(this,R.color.colorSecundario))
        boton.setOnClickListener{
            recreate()
        }
    }


}private fun crearBotonesDelTablero(
    context: Context,
    gridLayout: GridLayout,
    tablero: Tablero,
    filas: Int,
    columnas: Int,
    updater: UpdaterTextView
) {

    gridLayout.removeAllViews()
    gridLayout.rowCount = filas
    gridLayout.columnCount = columnas

    //contenido del gridlayout.
    for (i in 0 until filas){
        for (j in 0 until columnas){
            val boton = Button(context)
            boton.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                rowSpec = GridLayout.spec(i, 1f)
                columnSpec = GridLayout.spec(j, 1f)
            }

            boton.text = ""
            boton.textSize = 32f
            boton.setBackgroundColor(ContextCompat.getColor(context,R.color.azulMar))
            boton.setTextColor(ContextCompat.getColor(context,R.color.black))
            boton.setOnClickListener(
                CeldaClickListener(tablero.getCelda(i,j),updater)
            )
            gridLayout.addView(boton)
        }
    }
}



