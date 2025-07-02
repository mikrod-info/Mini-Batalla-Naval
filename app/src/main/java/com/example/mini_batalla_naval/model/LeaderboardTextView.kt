package com.example.mini_batalla_naval.model

import android.content.Context
import android.widget.TextView

object LeaderboardTextView {
    fun mostrarLeaderboard(context: Context, listaTextView: List<TextView>) {
        val leaderboard = LeaderboardManager.obtenerLeaderboard(context)
        for (i in listaTextView.indices){
            if (i < leaderboard.size) {
                val puntuacion = leaderboard[i]
                listaTextView[i].text = "${i + 1}. ${puntuacion.nombreJugador}: ${puntuacion.puntos}"
            } else {
                listaTextView[i].text = "" //o "-- vacio --" o "${i + 1}. -- vacio --"
            }
        }
    }

    fun actualizarLeaderboard(context: Context, nombreJugador: String, puntos: Int) {
        LeaderboardManager.guardarPuntuacion(context, nombreJugador, puntos)
    }
}