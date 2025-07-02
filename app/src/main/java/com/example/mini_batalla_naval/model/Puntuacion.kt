package com.example.mini_batalla_naval.model

/**
 * Clase de datos para representar una entrada en el ranking.
 *
 * @property nombreJugador El nombre del jugador.
 * @property puntos Los puntos obtenidos por el jugador.
 */
data class Puntuacion(
    val nombreJugador : String,
    val puntos : Int
) : Comparable<Puntuacion> {
    /**
     * Compara esta Puntuacion con otra para fines de ordenación.
     * El objetivo principal es ordenar por 'puntos' de forma descendente (mayor puntuación primero).
     * Si los puntos son iguales, se puede usar 'timestamp' para desempatar
     * (por ejemplo, la puntuación más reciente primero, o la más antigua primero).
     *
     * @param other La otra Puntuacion con la que comparar.
     * @return Un valor negativo si esta Puntuacion es "menor" que 'other' (debería ir después en orden descendente),
     *         cero si son "iguales" según el criterio principal,
     *         o un valor positivo si esta Puntuacion es "mayor" que 'other' (debería ir antes en orden descendente).
     */
    override fun compareTo(other: Puntuacion): Int {
        // Comparar por puntos en orden descendente.
        //    other.puntos.compareTo(this.puntos) logra esto:
        //    - Si other.puntos > this.puntos, devuelve > 0 (other va antes, o sea, this va después). Correcto para orden descendente de this.
        //    - Si other.puntos < this.puntos, devuelve < 0 (other va después, o sea, this va antes). Correcto para orden descendente de this.

        return other.puntos.compareTo(this.puntos)
    }
}
