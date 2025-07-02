package com.example.mini_batalla_naval

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mini_batalla_naval.model.LeaderboardTextView.mostrarLeaderboard
import com.example.mini_batalla_naval.model.LeaderboardTextView.actualizarLeaderboard

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var btnVolverLeaderboard: Button
    private lateinit var tvLeaderboardTitulo: TextView
    private lateinit var tvRank1: TextView
    private lateinit var tvRank2: TextView
    private lateinit var tvRank3: TextView
    private lateinit var tvRank4: TextView
    private lateinit var tvRank5: TextView
    private var nombreJugador: String = ""
    private var aciertos: Int = 0
    private var movimientos: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

        this.nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR") ?: ""
        this.movimientos = intent.getIntExtra("MOVIMIENTOS", 0)
        this.aciertos = intent.getIntExtra("ACIERTOS", 0)

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

        val puntos = movimientos//corregir con función que calcule un ratio con normalización barcos/dimensíon

        actualizarLeaderboard(this, nombreJugador, puntos)
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