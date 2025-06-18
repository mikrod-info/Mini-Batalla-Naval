package com.example.mini_batalla_naval.model

class Tablero(
    private var filas: Int,
    private var columnas: Int
) {
    private var cantidadBarcos: Int
    private var celdas: Array<Array<Celda>>

    init {
        //random para rango de [10, 15].
        //((Math.random() * (15-10+1 = 6)) + 10)
        //val (minBarcos, maxBarcos) = determinarRangoBarcos(filas)
        this.cantidadBarcos = randomEnRango(10, 15)
        //inicializa la matriz de celdas. al no pasar parámetros, se inicializa con celdas con valor false.
        celdas = Array(this.filas) { Array(this.columnas) { Celda() } }
        cargarBarcos()
    }

    //guardar y restaurar estado
    fun obtenerEstado(): TableroLogicoEstado {
        val matrizCeldasEstado = Array(this.filas) { f ->
            Array(this.columnas) { c ->
                val celdaActual = this.celdas[f][c]
                CeldaEstado(
                    esBarcoData = celdaActual.esBarco(),
                    reveladaData = celdaActual.esRevelada()
                )
            }
        }
        return TableroLogicoEstado(
            dimensionTableroData = this.filas,
            cantidadBarcosData = this.cantidadBarcos,
            celdasEstadoData = matrizCeldasEstado
        )
    }

    fun restaurarEstado(estado: TableroLogicoEstado) {
        this.filas = estado.dimensionTableroData
        this.columnas = estado.dimensionTableroData
        this.cantidadBarcos = estado.cantidadBarcosData
        this.celdas = Array(this.filas) { f ->
            Array(this.columnas) { c ->
                val celdaEstadoGuardada = estado.celdasEstadoData[f][c]
                Celda(
                    esBarco = celdaEstadoGuardada.esBarcoData,
                    revelada = celdaEstadoGuardada.reveladaData
                )
            }
        }
    }

    //métodos públicos

    fun fueAcierto(fila: Int, columna: Int): Boolean {
        return this.celdas[fila][columna].esBarco()
    }

    fun revelarBarco(fila: Int, columna: Int) {
        this.celdas[fila][columna].setRevelada()
    }

    fun getCantidadBarcos(): Int {
        return this.cantidadBarcos
    }

    //métodos auxiliares
//    private fun determinarRangoBarcos(filas: Int): Pair<Int, Int> {
//        return when (filas) {
//            6 -> Pair(10, 15)
//            8 -> Pair(20, 35)
//            10 -> Pair(30, 45)
//            else -> Pair(10, 15)
//        }
//    }

    private fun randomEnRango(min: Int, max: Int): Int {
        //fórmula para retornar un valor random en rango [A,B]: (Math.random() * (B-A+1)) + A)
        return (Math.random() * (max - min + 1)).toInt() + min
    }

    private fun cargarBarcos() {
        var barcosCargados = 0
        while (barcosCargados < this.cantidadBarcos) {
            //si la matriz de 6*6, el rango es [0,5]
            //((Math.random() * (5-0+1 = 6)) + 0)
            val filaRandom = randomEnRango(0, this.filas - 1)
            val columnaRandom = randomEnRango(0, this.columnas - 1)

            if (!fueAcierto(filaRandom, columnaRandom)) {
                this.celdas[filaRandom][columnaRandom].setBarco()
                barcosCargados++
            }
        }
    }

}

