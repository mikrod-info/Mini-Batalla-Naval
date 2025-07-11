package com.example.mini_batalla_naval.model

interface DialogListener {
    fun onDialogRestartGame()
    fun onDialogBackToHomeScreen()
    fun onDialogShowLeaderboard()
    fun onDialogShareScore(puntuacion: Puntuacion)
}