package com.example.mini_batalla_naval.model

import android.content.Context
import android.widget.TextView
import com.example.mini_batalla_naval.R

class UpdaterTextView(
    private val context: Context,
    private var tvRestantes: TextView,
    private var tvMovimientos: TextView,
    private var tvAciertos: TextView,
    private var tvMensajeJuego: TextView,
    private var cantidadBarcos: Int,
    private val gameEventListener: GameEventListener
) {
    private var valorRestante: Int
    private var valorMovimientos: Int
    private var valorAciertos: Int
    private var valorFallos: Int
    private val strRestante: String
    private val strMovimientos: String
    private val strAciertos: String
    private val strAgua: String
    private val strTocado: String
    private var strMensajeJuego: String

    init {
        //inicializa las variables contadoras.
        this.valorRestante = cantidadBarcos
        this.valorMovimientos = 0
        this.valorAciertos = 0
        this.valorFallos = 0
        //inicializa las variables de string.
        this.strRestante = this.context.getString(R.string.updater_restantes)
        this.strMovimientos = this.context.getString(R.string.updater_movimientos)
        this.strAciertos = this.context.getString(R.string.updater_aciertos)
        this.strAgua = this.context.getString(R.string.updater_agua)
        this.strTocado = this.context.getString(R.string.updater_tocado)
        this.strMensajeJuego = ""

        actualizarVistasContadores()
        actualizarVistaMensajeJuego()
    }

    //métodos para obtener y restaurar el estado de la clase
    fun obtenerEstado(): UpdaterEstado {
        return UpdaterEstado(
            cantidadBarcosData = this.cantidadBarcos,
            restantesData = this.valorRestante,
            movimientosData = this.valorMovimientos,
            aciertosData = this.valorAciertos,
            fallosData = this.valorFallos,
            mensajeJuegoData = this.strMensajeJuego
        )
    }

    fun restaurarEstado(estado: UpdaterEstado) {
        this.cantidadBarcos = estado.cantidadBarcosData
        this.valorRestante = estado.restantesData
        this.valorMovimientos = estado.movimientosData
        this.valorAciertos = estado.aciertosData
        this.valorFallos = estado.fallosData
        this.strMensajeJuego = estado.mensajeJuegoData

        actualizarVistasContadores()
        actualizarVistaMensajeJuego()
    }

    fun registrarActividad(fueAcierto: Boolean) {
        if (fueAcierto) incrementarAcierto() else incrementarFallo()

        mover()
        actualizarVistasContadores()
        actualizarVistaMensajeJuego()

        if (esVictoria()){
            this.tvMensajeJuego.text = ""
            this.gameEventListener.onGameWon()
        }
    }

    //métodos auxiliares

    private fun actualizarVistasContadores() {
        this.tvRestantes.text = String.format(this.strRestante, this.valorRestante)
        this.tvMovimientos.text = String.format(this.strMovimientos, this.valorMovimientos)
        this.tvAciertos.text = String.format(this.strAciertos, this.valorAciertos)
    }

    private fun actualizarVistaMensajeJuego() {
        this.tvMensajeJuego.text = this.strMensajeJuego
    }

    private fun mover() {
        this.valorMovimientos++
    }

    private fun incrementarAcierto() {
        this.strMensajeJuego = this.strTocado
        this.valorRestante--
        this.valorAciertos++
    }

    private fun incrementarFallo() {
        this.strMensajeJuego = this.strAgua
        this.valorFallos++
    }

    private fun esVictoria(): Boolean {
        return this.valorAciertos == this.cantidadBarcos
    }

    fun getMovimientos(): Int {
        return this.valorMovimientos
    }
    fun getAciertos(): Int {
        return this.valorAciertos
    }

    fun getCantidadBarcos(): Int {
        return this.cantidadBarcos
    }
}
