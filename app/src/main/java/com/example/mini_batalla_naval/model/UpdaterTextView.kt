package com.example.mini_batalla_naval.model

import android.content.Context
import android.widget.TextView
import com.example.mini_batalla_naval.R

class UpdaterTextView(
    context: Context,
    private val tvRestantes: TextView,
    private val tvMovimientos: TextView,
    private val tvAciertos: TextView,
    private val cantidadBarcos: Int
) {
    private var valorRestante: Int
    private var valorMovimientos: Int
    private var valorAciertos: Int
    private val strRestante: String
    private val strMovimientos: String
    private val strAciertos: String

    init {
        this.valorRestante = 0
        this.valorMovimientos = 0
        this.valorAciertos = 0
        this.strRestante = context.getString(R.string.updater_restantes)
        this.strMovimientos = context.getString(R.string.updater_movimientos)
        this.strAciertos = context.getString(R.string.updater_aciertos)

        this.inicializarContadores()
        this.actualizarVistas()
    }

    private fun inicializarContadores() {
        this.valorRestante = this.cantidadBarcos
        this.valorMovimientos = 0
        this.valorAciertos = 0
    }

    private fun actualizarVistas() {
        this.tvRestantes.text = String.format(this.strRestante, this.valorRestante)
        this.tvMovimientos.text = String.format(this.strMovimientos, this.valorMovimientos)
        this.tvAciertos.text = String.format(this.strAciertos, this.valorAciertos)
    }

    fun actualizar() {
        this.valorMovimientos++
        actualizarVistas()
    }

    fun incrementarAcierto() {
        this.valorRestante--
        this.valorAciertos++
    }
}
