package com.example.mini_batalla_naval.model

object GameSettings {
    // default settings asume que no se elijió ninguna opción (tablero 6*6)
    const val DEFAULT_DIMENSION = 6
    const val DEFAULT_TIME = 20
    const val DEFAULT_EMOJI_SIZE = 24f
    const val DEFAULT_DIFICULTAD = 0.6


    fun getDimensionSegunOpcion(opcion: Int): Int {
        return when (opcion) {
            0 -> 6
            1 -> 8
            2 -> 10
            else -> DEFAULT_DIMENSION
        }
    }

    fun getTiempoSegunDimension(dimension: Int): Int {
        return when (dimension) {
            6 -> 20
            8 -> 25
            10 -> 30
            else -> DEFAULT_TIME
        }
    }

    fun getTamanioEmojiSegunDimension(dimension: Int): Float {
        return when (dimension) {
            6 -> 24f
            8 -> 20f
            10 -> 16f
            else -> DEFAULT_EMOJI_SIZE
        }
    }

    fun getDificultadSegunDimension(dimension: Int): Double {
        return when(dimension) {
            6 -> 0.6
            8 -> 0.8
            10 -> 1.0
            else -> DEFAULT_DIFICULTAD
        }
    }
}