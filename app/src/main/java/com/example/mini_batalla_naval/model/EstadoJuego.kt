package com.example.mini_batalla_naval.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CeldaEstado(
    val esBarcoData: Boolean,
    val reveladaData: Boolean
) : Parcelable

@Parcelize
data class TableroLogicoEstado(
    val dimensionTableroData: Int,
    val cantidadBarcosData: Int,
    val celdasEstadoData: Array<Array<CeldaEstado>>
) : Parcelable {
    //Necesario para que Array<Array<...>> funcione bien con data class equals/hashCode
    //por si hace falta comparar TableroEstado directamente.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TableroLogicoEstado

        if (this.dimensionTableroData != other.dimensionTableroData) return false
        if (this.cantidadBarcosData != other.cantidadBarcosData) return false
        if (!this.celdasEstadoData.contentDeepEquals(other.celdasEstadoData)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = this.dimensionTableroData
        result = 31 * result + this.cantidadBarcosData
        result = 31 * result + this.celdasEstadoData.contentDeepHashCode()
        return result
    }
}

@Parcelize
data class UpdaterEstado(
    val cantidadBarcosData: Int,
    val restantesData: Int,
    val movimientosData: Int,
    val aciertosData: Int,
    val fallosData: Int,
    val mensajeJuegoData: String
) : Parcelable

@Parcelize
data class JuegoEstado(
    val nombreJugadorData: String,
    val segundosRestantesData: Int,
    val dimensionTableroData: Int,
    val juegoTerminadoData: Boolean,
    val tableroLogicoData: TableroLogicoEstado,
    val updaterData: UpdaterEstado,
    val tableroVisualData: ArrayList<BotonVisualEstado>
) : Parcelable

@Parcelize
data class BotonVisualEstado(
    val isEnabledData: Boolean,
    val textoData: String
) : Parcelable

