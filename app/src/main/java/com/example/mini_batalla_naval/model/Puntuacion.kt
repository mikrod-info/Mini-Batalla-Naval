package com.example.mini_batalla_naval.model

import com.example.mini_batalla_naval.model.GameSettings.getDificultadSegunDimension

data class Puntuacion(
    private val nombreJugador : String,
    private val aciertos: Int,
    private val movimientos: Int,
    private val cantidadBarcos: Int,
    private val dimensionTablero: Int
) : Comparable<Puntuacion> {
    private var puntos : Double = 0.0

    init {
        val eficiencia = movimientos / cantidadBarcos.toDouble()
        val dificultad = getDificultadSegunDimension(dimensionTablero)
        this.puntos = eficiencia * dificultad

    }

    override fun compareTo(other: Puntuacion): Int {
        // Si other.puntos > this.puntos, devuelve > 0 (other va antes, o sea, this va después)
        // Si other.puntos < this.puntos, devuelve < 0 (this va antes, o sea, other va después)
        return this.puntos.compareTo(other.puntos)
    }

    fun getNombreJugador(): String {
        return this.nombreJugador
    }

    fun getAciertos(): Int {
        return this.aciertos
    }

    fun getMovimientos(): Int {
        return this.movimientos
    }

    fun getPuntos(): Double {
        return this.puntos
    }

    fun getCantidadBarcos(): Int {
        return this.cantidadBarcos
    }

    fun getDimensionTablero(): Int {
        return this.dimensionTablero
    }

}
