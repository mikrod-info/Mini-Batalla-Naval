package com.example.mini_batalla_naval.model

import android.content.Context
import android.widget.TextView

object LeaderboardTextView {
    fun mostrarLeaderboard(context: Context, listaTextView: List<TextView>) {
        val leaderboard = LeaderboardManager.obtenerLeaderboard(context)
        for (i in listaTextView.indices) {
            if (i < leaderboard.size) {
                val puntuacion = leaderboard[i]
                listaTextView[i].text =
                    "${i + 1}. ${puntuacion.getNombreJugador()}: ${puntuacion.getAciertos()} aciertos/ ${puntuacion.getMovimientos()} movimientos."
            } else {
                listaTextView[i].text = "" //o "-- vacio --" o "${i + 1}. -- vacio --"
            }
        }
    }

    fun actualizarLeaderboard(context: Context, ultimaPuntuacion: Puntuacion) {
        LeaderboardManager.guardarPuntuacion(context,ultimaPuntuacion)
    }
}