package com.example.mini_batalla_naval.model

internal class Celda(
    private var estaOcupada: Boolean = false
) {

    fun estaOcupada(): Boolean {
        return this.estaOcupada
    }

    fun ocupar() {
        this.estaOcupada = true
    }
}
