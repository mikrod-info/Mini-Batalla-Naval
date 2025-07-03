package com.example.mini_batalla_naval

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mini_batalla_naval.model.LeaderboardTextView.mostrarLeaderboard
import com.example.mini_batalla_naval.model.Puntuacion

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var btnVolverLeaderboard: Button
    private lateinit var tvLeaderboardTitulo: TextView
    private lateinit var tvRank1: TextView
    private lateinit var tvRank2: TextView
    private lateinit var tvRank3: TextView
    private lateinit var tvRank4: TextView
    private lateinit var tvRank5: TextView
    private lateinit var ultimaPuntuacion: Puntuacion
    private var nombreJugador: String = ""
    private var aciertos: Int = 0
    private var movimientos: Int = 0
    private var cantidadBarcos: Int = 0
    private var dimensionTablero: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        this.nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR") ?: ""
        this.aciertos = intent.getIntExtra("ACIERTOS", 0)
        this.movimientos = intent.getIntExtra("MOVIMIENTOS", 0)
        this.cantidadBarcos = intent.getIntExtra("CANTIDAD_BARCOS", 0)
        this.dimensionTablero = intent.getIntExtra("DIMENSION_TABLERO", 0)
        this.ultimaPuntuacion = Puntuacion(
            this.nombreJugador,
            this.aciertos,
            this.movimientos,
            this.cantidadBarcos,
            this.dimensionTablero
        )

        setupViews()
        setupBtnVolverLeaderboard()

        val listaTextView =
            listOf(
                this.tvRank1,
                this.tvRank2,
                this.tvRank3,
                this.tvRank4,
                this.tvRank5
            )

        mostrarLeaderboard(this, listaTextView)

    }

    private fun setupBtnVolverLeaderboard() {
        this.btnVolverLeaderboard.setOnClickListener {
            finish()
        }
    }

    private fun setupViews() {
        this.btnVolverLeaderboard = findViewById<Button>(R.id.btnVolverLeaderboard)
        this.tvLeaderboardTitulo = findViewById<TextView>(R.id.tvLeaderboardTitulo)
        this.tvRank1 = findViewById<TextView>(R.id.tvRank1)
        this.tvRank2 = findViewById<TextView>(R.id.tvRank2)
        this.tvRank3 = findViewById<TextView>(R.id.tvRank3)
        this.tvRank4 = findViewById<TextView>(R.id.tvRank4)
        this.tvRank5 = findViewById<TextView>(R.id.tvRank5)
    }
}