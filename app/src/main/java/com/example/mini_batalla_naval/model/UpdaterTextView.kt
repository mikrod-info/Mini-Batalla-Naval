package com.example.mini_batalla_naval.model

import android.content.Context
import android.widget.TextView
import com.example.mini_batalla_naval.R

class UpdaterTextView(
    context: Context,
    private val tvRestantes: TextView,
    private val tvMovimientos: TextView,
    private val tvAciertos: TextView,
    private val tvMensajeJuego: TextView,
    private val cantidadBarcos: Int,
    private var winEventListener: WinEventListener
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
    private val strMensajeVictoria: String
    private var strMensajeJuego: String

    init {
        //inicializa las variables contadoras.
        this.valorRestante = cantidadBarcos
        this.valorMovimientos = 0
        this.valorAciertos = 0
        this.valorFallos = 0
        //inicializa las variables de string.
        this.strRestante = context.getString(R.string.updater_restantes)
        this.strMovimientos = context.getString(R.string.updater_movimientos)
        this.strAciertos = context.getString(R.string.updater_aciertos)
        this.strAgua = context.getString(R.string.updater_agua)
        this.strTocado = context.getString(R.string.updater_tocado)
        this.strMensajeVictoria = context.getString(R.string.updater_victoria)
        this.strMensajeJuego = ""

        this.actualizarVistasContadores()
    }

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

    private fun reiniciarStrings() {
        this.tvRestantes.text = ""
        this.tvMovimientos.text = ""
        this.tvAciertos.text = ""
        this.tvMensajeJuego.text = ""
    }

    private fun informarVictoria(){
        this.strMensajeJuego = String.format(this.strMensajeVictoria, this.valorMovimientos, this.valorAciertos, this.valorFallos)
        actualizarVistaMensajeJuego()
    }

    fun registrarActividad(fueAcierto: Boolean) {
           if (fueAcierto) incrementarAcierto() else incrementarFallo()

           mover()
           actualizarVistasContadores()
           actualizarVistaMensajeJuego()

           if (esVictoria()) {
               reiniciarStrings()
               informarVictoria()
               this.winEventListener.onGameWon()
           }
    }

    //En caso de necesitarlos para guardar y cargar el estado del juego.
//    fun getRestantes(): Int = this.valorRestante
//    fun getMovimientos(): Int = this.valorMovimientos
//    fun getAciertos(): Int = this.valorAciertos
//    fun getFallos(): Int = this.valorFallos
//
//    fun setEstado(restantes: Int, movimientos: Int, aciertos: Int, fallos: Int){
//        this.valorRestante = restantes
//        this.valorMovimientos = movimientos
//        this.valorAciertos = aciertos
//        this.valorFallos = fallos
//        actualizarVistasContadores()
//    }

}
