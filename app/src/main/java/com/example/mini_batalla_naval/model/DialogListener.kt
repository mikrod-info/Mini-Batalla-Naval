package com.example.mini_batalla_naval.model

interface DialogListener {
    fun onDialogRestartGame()
    fun onDialogBackToHomeScreen()
    fun onDialogShowLeaderboard(puntuacion: Puntuacion)
    fun onDialogShareScore(puntuacion: Puntuacion)
}