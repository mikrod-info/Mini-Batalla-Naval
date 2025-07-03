package com.example.mini_batalla_naval.model

/**
 * Clase de datos para representar una entrada en el ranking.
 *
 * @property nombreJugador El nombre del jugador.
 * @property puntos Los puntos obtenidos por el jugador.
 */
data class Puntuacion(
    private val nombreJugador : String,
    private val aciertos: Int,
    private val movimientos: Int,
    private val cantidadBarcos: Int,
    private val dimensionTablero: Int
) : Comparable<Puntuacion> {
    private var puntos : Double = 0.0

    init {
        val eficiencia = this.aciertos / this.movimientos.toDouble()
        val dificultad = (this.dimensionTablero * this.dimensionTablero) / this.cantidadBarcos.toDouble()
        this.puntos = eficiencia * dificultad
    }

    override fun compareTo(other: Puntuacion): Int {
        // Si other.puntos > this.puntos, devuelve > 0 (other va antes, o sea, this va después)
        // Si other.puntos < this.puntos, devuelve < 0 (other va después, o sea, this va antes)
        return other.puntos.compareTo(this.puntos)
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
