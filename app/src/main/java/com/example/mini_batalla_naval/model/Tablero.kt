package com.example.mini_batalla_naval.model

class Tablero(
    private val filas: Int,
    private val columnas: Int
) {
    private val cantidadBarcos: Int
    private val celdas: Array<Array<Celda>>

    init {
        //random para rango de [10, 15].
        //((Math.random() * (15-10+1 = 6)) + 10)
        this.cantidadBarcos = randomEnRango(15,10)
        //inicializa la matriz de celdas. al no pasar parámetros, se inicializa con celdas con valor false.
        celdas = Array (this.filas) { Array(this.columnas) { Celda() } }
        cargarBarcos()
    }

    fun celdaOcupada(fila: Int, columna: Int): Boolean {
        return this.celdas[fila][columna].estaOcupada()
    }

    fun getCantidadBarcos(): Int {
        return this.cantidadBarcos
    }

    private fun cargarBarcos() {
        var barcosCargados = 0
        while (barcosCargados < this.cantidadBarcos) {
            //el rango acá es de [0,5] en matriz de 6*6
            //((Math.random() * (5-0+1 = 6)) + 0)
            val filaRandom = randomEnRango(0,this.filas-1)
            val columnaRandom = randomEnRango(0,this.columnas-1)

            if (!celdas[filaRandom][columnaRandom].estaOcupada()) {
                celdas[filaRandom][columnaRandom].ocupar()
                barcosCargados++
            }
        }
    }

    private fun randomEnRango(min: Int, max: Int): Int {
        //fórmula para retornar un valor random en rango [A,B]: (Math.random() * (B-A+1)) + A)
        return (Math.random() * (max - min + 1)).toInt() + min
    }
}
