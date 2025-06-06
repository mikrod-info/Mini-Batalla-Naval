package com.example.mini_batalla_naval.model

class Celda() {
    private var revelada: Boolean = false
    private var esBarco: Boolean = false

    constructor(esBarco: Boolean, revelada: Boolean) : this() {
        this.esBarco = esBarco
        this.revelada = revelada
    }

    fun esBarco(): Boolean {
        return this.esBarco
    }

    fun setBarco() {
        this.esBarco = true
    }

    fun setRevelada() {
        this.revelada = true
    }

    fun esRevelada(): Boolean {
        return this.revelada
    }
}
