package com.example.mini_batalla_naval.model

interface GameEventListener {
    fun onGameWon()
    fun onGameRestart()
}