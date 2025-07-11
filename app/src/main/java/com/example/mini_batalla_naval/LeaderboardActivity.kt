package com.example.mini_batalla_naval

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mini_batalla_naval.model.LeaderboardManager.obtenerLeaderboard

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var btnVolverLeaderboard: Button
    private lateinit var tvLeaderboardTitulo: TextView
    private lateinit var tvRank1: TextView
    private lateinit var tvRank2: TextView
    private lateinit var tvRank3: TextView
    private lateinit var tvRank4: TextView
    private lateinit var tvRank5: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboard)

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

        mostrarLeaderboard(listaTextView)

    }

    private fun setupBtnVolverLeaderboard() {
        this.btnVolverLeaderboard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
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

    private fun mostrarLeaderboard(listaTextView: List<TextView>) {
        val formato = this.getString(R.string.leaderboard_text_template)
        val leaderboard = obtenerLeaderboard(this)
        for (i in listaTextView.indices) {
            if (i < leaderboard.size) {
                val puntuacion = leaderboard[i]
                listaTextView[i].text =
                    String.format(
                        formato,
                        (i + 1),
                        puntuacion.getNombreJugador(),
                        puntuacion.getAciertos(),
                        puntuacion.getMovimientos()
                    )
            } else {
                listaTextView[i].text = ""
            }
        }
    }
}