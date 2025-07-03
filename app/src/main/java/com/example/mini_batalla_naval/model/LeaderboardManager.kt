package com.example.mini_batalla_naval.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.collections.toMutableList
import androidx.core.content.edit


object LeaderboardManager {
    private const val LEADERBOARD_PREFS = "LeaderboardPrefs"
    private const val KEY_LEADERBOARD = "leaderboard_list"
    private const val MAX_ENTRIES = 5
    private val gson = Gson()

    /**
     * Obtiene y devuelve la instancia de SharedPreferences específica para el leaderboard.
     * @param context El Contexto de la aplicación, necesario para acceder a SharedPreferences.
     * @return La instancia de SharedPreferences para "LeaderboardPrefs".
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(LEADERBOARD_PREFS, Context.MODE_PRIVATE)
    }

    fun guardarPuntuacion(context: Context, nuevaPuntuacion: Puntuacion) {
        val sharedPreferences = getSharedPreferences(context)
        val listaPuntuaciones = obtenerLeaderboard(context).toMutableList()
        listaPuntuaciones.add(nuevaPuntuacion)
        listaPuntuaciones.sortDescending()
        val nuevaListaPuntuaciones =
            if (listaPuntuaciones.size > MAX_ENTRIES) listaPuntuaciones.subList(0, MAX_ENTRIES) else listaPuntuaciones
        sharedPreferences.edit {
            val json = gson.toJson(nuevaListaPuntuaciones)
            putString(KEY_LEADERBOARD, json)
            apply()
        }
    }

    fun obtenerLeaderboard(context: Context): List<Puntuacion> {
        val sharedPreferences = getSharedPreferences(context)
        val json = sharedPreferences.getString(KEY_LEADERBOARD, null)

        return if (json != null) {
            val type = object : TypeToken<List<Puntuacion>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun entraAlRanking(context: Context, ultimaPuntuacion: Puntuacion): Boolean {
        val listaPuntuaciones = obtenerLeaderboard(context)

        if (listaPuntuaciones.isEmpty()) return true
        if (listaPuntuaciones.size < MAX_ENTRIES) return true

        return ultimaPuntuacion.getPuntos() < listaPuntuaciones.last().getPuntos()
    }

}