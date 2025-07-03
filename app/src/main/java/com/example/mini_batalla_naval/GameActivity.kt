package com.example.mini_batalla_naval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.mini_batalla_naval.model.UpdaterTextView
import com.example.mini_batalla_naval.model.Tablero
import com.example.mini_batalla_naval.model.GameEventListener
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import com.example.mini_batalla_naval.model.BotonVisualEstado
import com.example.mini_batalla_naval.model.JuegoEstado
import com.example.mini_batalla_naval.model.LeaderboardManager
import com.example.mini_batalla_naval.model.LeaderboardTextView.actualizarLeaderboard
import com.example.mini_batalla_naval.model.Puntuacion

class GameActivity : AppCompatActivity(), GameEventListener {
    private lateinit var glTablero: GridLayout
    private lateinit var tableroLogico: Tablero
    private lateinit var updater: UpdaterTextView
    private lateinit var tvRestantes: TextView
    private lateinit var tvMovimientos: TextView
    private lateinit var tvAciertos: TextView
    private lateinit var tvMensajeJuego: TextView
    private lateinit var tvTiempoRestante: TextView
    private lateinit var tvNombreJugador: TextView
    private lateinit var btnReiniciar: Button
    private lateinit var btnShowPopup: ImageButton
    private lateinit var ultimaPuntuacion: Puntuacion

    private val KEY_JUEGO_ESTADO = "KEY_JUEGO_ESTADO"
    private var juegoTerminado: Boolean = false
    private var dimensionTablero: Int = 6
    private var nombreJugador: String = "Anónimo"

    private var countDownTimer: CountDownTimer? = null
    private var tiempoTotalSegundos = 20
    private var segundosRestantes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Captura datos del Intent de MainActivity
        this.dimensionTablero = intent.getIntExtra("DIMENSION_TABLERO", 6)
        this.nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR") ?: "Anónimo"

        tiempoTotalSegundos = when (dimensionTablero) {
            6 -> 20
            8 -> 25
            10 -> 60
            else -> 20
        }
        segundosRestantes = tiempoTotalSegundos


        if (savedInstanceState != null) {

            val estadoJuego: JuegoEstado? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    savedInstanceState.getParcelable(KEY_JUEGO_ESTADO, JuegoEstado::class.java)
                } else {
                    @Suppress("DEPRECATION") // Suprime la advertencia para versiones anteriores a API 33
                    savedInstanceState.getParcelable<JuegoEstado>(KEY_JUEGO_ESTADO)
                }

            if (estadoJuego != null) {
                this.juegoTerminado = estadoJuego.juegoTerminadoData
                this.dimensionTablero = estadoJuego.dimensionTableroData

                inicializarJuego()

                this.tableroLogico.restaurarEstado(estadoJuego.tableroLogicoData)
                this.updater.restaurarEstado(estadoJuego.updaterData)

                crearTableroVisualDelEstado(estadoJuego.tableroVisualData)

                if (this.juegoTerminado) {
                    deshabilitarTablero()
                }
            } else inicializarJuego()

        } else inicializarJuego()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val estadoTablero = this.tableroLogico.obtenerEstado()
        val estadoUpdater = this.updater.obtenerEstado()
        val estadoTableroVisual = ArrayList<BotonVisualEstado>()
        for (i in 0 until this.glTablero.childCount) {
            val botonView = this.glTablero.getChildAt(i) as Button
            estadoTableroVisual.add(
                BotonVisualEstado(
                    botonView.isEnabled,
                    botonView.text.toString()
                )
            )

        }

        val estadoJuego = JuegoEstado(
            dimensionTableroData = this.dimensionTablero,
            juegoTerminadoData = this.juegoTerminado,
            tableroLogicoData = estadoTablero,
            updaterData = estadoUpdater,
            tableroVisualData = estadoTableroVisual
        )

        outState.putParcelable(KEY_JUEGO_ESTADO, estadoJuego)
    }

    private fun inicializarJuego() {
        inicializarVistas()
        crearBotonesListeners()
        setupTablero()
        tvTiempoRestante.text = "Tiempo: ${segundosRestantes}s"
        iniciarTemporizador()

    }

    private fun inicializarVistas() {
        this.glTablero = findViewById<GridLayout>(R.id.glTablero)
        this.tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        this.tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        this.tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        this.tvMensajeJuego = findViewById<TextView>(R.id.tvMensajeJuego)
        this.tvNombreJugador = findViewById<TextView>(R.id.tvNombreJugador)
        this.tvTiempoRestante = findViewById<TextView>(R.id.tvTiempoRestante)

        this.btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        this.btnShowPopup = findViewById<ImageButton>(R.id.btnGameHelp)
    }

    private fun setupTablero() {
        this.tvNombreJugador.text = "Capitán ${this.nombreJugador}"
        this.tableroLogico = Tablero(dimensionTablero, dimensionTablero)
        this.updater = UpdaterTextView(
            this,
            this.tvRestantes,
            this.tvMovimientos,
            this.tvAciertos,
            this.tvMensajeJuego,
            this.tableroLogico.getCantidadBarcos(),
            this
        )

        crearTableroVisual(dimensionTablero, dimensionTablero)
    }

    private fun crearBotonesListeners() {
        this.btnReiniciar.setOnClickListener {
            onGameRestart()
        }

        this.btnShowPopup.setOnClickListener { it ->
            showGamePopupMenu(it)
        }
    }

    private fun crearTableroVisual(
        filas: Int,
        columnas: Int,
    ) {
        //esta función crea botones con event listener que invocan internamente al updater 1 por botón 1 sola vez
        this.glTablero.removeAllViews()
        this.glTablero.rowCount = filas
        this.glTablero.columnCount = columnas

        val emojiAgua = this.getString(R.string.emoji_agua)
        val emojiBarco = this.getString(R.string.emoji_barco)
        val tamanioEmoji = when (filas) {
            6 -> 24f
            8 -> 20f
            10 -> 16f
            else -> 24f
        }

        //contenido del glTablero(gridlayout).
        for (i in 0 until filas) {
            for (j in 0 until columnas) {
                //crear botón y agregar a gridlayout. el estilo viene de styles.xml
                val boton = Button(this, null, 0, R.style.estilo_boton_tablero)
                boton.text = ""
                boton.textSize = tamanioEmoji
                boton.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }

                boton.setOnClickListener {
                    //control de ejecución de evento. Esto impide que se ejecute más de una vez.
                    if (this.juegoTerminado || !it.isEnabled) return@setOnClickListener

                    val fueAcierto = this.tableroLogico.fueAcierto(i, j)
                    boton.text = if (fueAcierto) emojiBarco else emojiAgua
                    if (fueAcierto) this.tableroLogico.revelarBarco(i, j)
                    updater.registrarActividad(fueAcierto)
                    it.isEnabled = false
                }

                this.glTablero.addView(boton)
            }
        }
    }

    private fun crearTableroVisualDelEstado(estadoBotones: ArrayList<BotonVisualEstado>) {
        this.glTablero.removeAllViews()
        this.glTablero.rowCount = this.dimensionTablero
        this.glTablero.columnCount = this.dimensionTablero

        val tamanioEmoji = when (this.dimensionTablero) {
            6 -> 24f
            8 -> 20f
            10 -> 16f
            else -> 24f
        }

        var estadoIndex = 0
        for (i in 0 until this.dimensionTablero) {
            for (j in 0 until this.dimensionTablero) {
                val boton = Button(this, null, 0, R.style.estilo_boton_tablero)
                val emojiAgua = this.getString(R.string.emoji_agua)
                val emojiBarco = this.getString(R.string.emoji_barco)

                if (estadoIndex < estadoBotones.size) {
                    val estadoBoton = estadoBotones[estadoIndex]
                    boton.isEnabled = estadoBoton.isEnabledData
                    boton.textSize = tamanioEmoji
                    boton.text = estadoBoton.textoData
                    estadoIndex++
                } else {
                    boton.text = ""
                }

                boton.layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    rowSpec = GridLayout.spec(i, 1f)
                    columnSpec = GridLayout.spec(j, 1f)
                }

                boton.setOnClickListener {
                    //control de ejecución de evento. Esto impide que se ejecute más de una vez.
                    if (this.juegoTerminado || !it.isEnabled) return@setOnClickListener

                    if (it.isEnabled) {
                        val fueAcierto = this.tableroLogico.fueAcierto(i, j)
                        boton.text = if (fueAcierto) emojiBarco else emojiAgua
                        updater.registrarActividad(fueAcierto)
                        it.isEnabled = false
                    }
                }

                this.glTablero.addView(boton)
            }

        }

    }

    private fun deshabilitarTablero() {
        for (i in 0 until this.glTablero.childCount) {
            val boton = this.glTablero.getChildAt(i)
            if (boton.isEnabled) boton.isEnabled = false
        }
    }

    private fun showGamePopupMenu(view: View) {
        val popup = PopupMenu(this, view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_popup, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.opInicio -> {
                    finish()
                    true
                }

                R.id.opAyuda -> {
                    // Acción para "Ayuda": Abrir HelpActivity
                    val intent = Intent(this, HelpActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun iniciarTemporizador() {
        countDownTimer?.cancel()
        tvTiempoRestante.text = "Tiempo: ${segundosRestantes}s" // ← Mostrar antes de que empiece

        countDownTimer = object : CountDownTimer(tiempoTotalSegundos * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                segundosRestantes = (millisUntilFinished / 1000).toInt()
                tvTiempoRestante.text = "Tiempo: ${segundosRestantes}s"
            }

            override fun onFinish() {
                if (!juegoTerminado) {
                    juegoTerminado = true
                    onGameOver()
                }
            }
        }.start()
    }

    //Diálogos victoria/derrota
    private fun mostrarDialogoVictoria() {
        AlertDialog.Builder(this@GameActivity)
            .setTitle("🎉 ¡Ganaste!")
            .setMessage("Encontraste todos los barcos a tiempo, pero no entraste al Top 5")
            .setPositiveButton("¡Intentar de nuevo!") { _, _ -> onGameRestart() }
            .setNegativeButton("¡Ir al inicio!") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    private fun mostrarDialogoVictoriaLeaderboard(ultimaPuntuacion: Puntuacion) {
        AlertDialog.Builder(this@GameActivity)
            .setTitle("🎉 ¡Ganaste!")
            .setMessage("Encontraste todos los barcos a tiempo y accediste al ranking!")
            .setPositiveButton("¡Jugar de nuevo!") { _, _ -> onGameRestart() }
            .setNegativeButton("¡Ver ranking!") { _, _ -> irLeaderboard(ultimaPuntuacion) }
            .setNeutralButton("Compartir mi puntuación") { _, _ ->
                compartirPuntuacion(
                    ultimaPuntuacion
                )
            }
            .setCancelable(false)
            .show()
    }

    private fun compartirPuntuacion(puntuacion: Puntuacion) {
        val dimension = puntuacion.getDimensionTablero()
        val texto = """
        🚢 Mini Batalla Naval
        🎉 ¡${puntuacion.getNombreJugador()} ganó!
        ✅ Aciertos: ${puntuacion.getAciertos()}
        🔁 Movimientos: ${puntuacion.getMovimientos()}
        📐 Tablero: ${dimension}x${dimension}
        """.trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, texto)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, "Compartir puntaje"))
    }

    private fun mostrarDialogoDerrota() {
        AlertDialog.Builder(this@GameActivity)
            .setTitle("⏱ Tiempo agotado")
            .setMessage("No lograste encontrar todos los barcos a tiempo.")
            .setPositiveButton("¡Intentar de nuevo!") { _, _ -> onGameRestart() }
            .setNegativeButton("Volver a Inicio") { _, _ -> finish() }
            .setCancelable(false)
            .show()
    }

    fun irLeaderboard(ultimaPuntuacion: Puntuacion) {
        val intent = Intent(this, LeaderboardActivity::class.java)
        intent.putExtra("NOMBRE_JUGADOR", ultimaPuntuacion.getNombreJugador())
        intent.putExtra("ACIERTOS", ultimaPuntuacion.getAciertos())
        intent.putExtra("MOVIMIENTOS", ultimaPuntuacion.getMovimientos())
        intent.putExtra("CANTIDAD_BARCOS", ultimaPuntuacion.getCantidadBarcos())
        intent.putExtra("DIMENSION_TABLERO", ultimaPuntuacion.getDimensionTablero())
        startActivity(intent)
    }

    // Implementaciones de la interfaz GameEventListener
    override fun onGameWon() {
        this.ultimaPuntuacion = Puntuacion(
            this.nombreJugador,
            this.updater.getAciertos(),
            this.updater.getMovimientos(),
            this.updater.getCantidadBarcos(),
            this.dimensionTablero
        )
        this.juegoTerminado = true
        this.countDownTimer?.cancel()
        deshabilitarTablero()
        if (LeaderboardManager.entraAlRanking(this, this.ultimaPuntuacion)) {
            actualizarLeaderboard(this, this.ultimaPuntuacion)
            mostrarDialogoVictoriaLeaderboard(this.ultimaPuntuacion)
        } else mostrarDialogoVictoria()
    }

    override fun onGameRestart() {
        juegoTerminado = false
        setupTablero()
        segundosRestantes = tiempoTotalSegundos
        iniciarTemporizador()
    }

    override fun onGameOver() {
        this.juegoTerminado = true
        countDownTimer?.cancel()
        deshabilitarTablero()
        mostrarDialogoDerrota()
    }
}