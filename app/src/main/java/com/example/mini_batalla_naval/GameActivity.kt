package com.example.mini_batalla_naval

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.example.mini_batalla_naval.model.UpdaterTextView
import com.example.mini_batalla_naval.model.TableroLogico
import com.example.mini_batalla_naval.model.GameEventListener
import com.example.mini_batalla_naval.model.GameSettings.getTiempoSegunDimension
import com.example.mini_batalla_naval.model.GameSettings.getTamanioEmojiSegunDimension
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import com.example.mini_batalla_naval.model.BotonVisualEstado
import com.example.mini_batalla_naval.model.DialogListener
import com.example.mini_batalla_naval.model.DialogManager
import com.example.mini_batalla_naval.model.GameSettings
import com.example.mini_batalla_naval.model.JuegoEstado
import com.example.mini_batalla_naval.model.LeaderboardManager
import com.example.mini_batalla_naval.model.LeaderboardManager.guardarPuntuacion
import com.example.mini_batalla_naval.model.Puntuacion
import com.example.mini_batalla_naval.model.TimerInterface
import com.example.mini_batalla_naval.model.TimerManager

class GameActivity : AppCompatActivity(), GameEventListener, TimerInterface, DialogListener {
    private lateinit var glTablero: GridLayout
    private lateinit var tableroLogico: TableroLogico
    private lateinit var timer: TimerManager
    private lateinit var updater: UpdaterTextView
    private lateinit var dialog: DialogManager
    private lateinit var tvRestantes: TextView
    private lateinit var tvMovimientos: TextView
    private lateinit var tvAciertos: TextView
    private lateinit var tvMensajeJuego: TextView
    private lateinit var tvTimer: TextView
    private lateinit var tvNombreJugador: TextView
    private lateinit var btnReiniciar: Button
    private lateinit var btnShowPopup: ImageButton
    private lateinit var ultimaPuntuacion: Puntuacion

    private val KEY_JUEGO_ESTADO = "KEY_JUEGO_ESTADO"
    private var nombreJugador: String = ""
    private var juegoTerminado: Boolean = false
    private var dimensionTablero: Int = GameSettings.DEFAULT_DIMENSION

    //    private var countDownTimer: CountDownTimer? = null
    private var tiempoConfigurado = GameSettings.DEFAULT_TIME
//    private var segundosRestantes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        // Captura datos del Intent de MainActivity
        this.dimensionTablero =
            intent.getIntExtra("DIMENSION_TABLERO", GameSettings.DEFAULT_DIMENSION)
        this.nombreJugador = intent.getStringExtra("NOMBRE_JUGADOR") ?: ""
        // Nuevo tiempo total en función de la dimensión del tablero
        this.tiempoConfigurado = getTiempoSegunDimension(this.dimensionTablero)

        inicializarVistas()
        setupButtonListeners()

        if (savedInstanceState != null) {
            // Traer el parcelable del estado del juego
            val estadoJuego: JuegoEstado? =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    savedInstanceState.getParcelable(KEY_JUEGO_ESTADO, JuegoEstado::class.java)
                } else {
                    @Suppress("DEPRECATION") // Suprime la advertencia para versiones anteriores a API 33
                    savedInstanceState.getParcelable<JuegoEstado>(KEY_JUEGO_ESTADO)
                }

            if (estadoJuego != null) {
                // Restaurar juego
                restaurarJuegoDesdeEstado(estadoJuego)

                if (this.juegoTerminado) deshabilitarTablero()

            } else inicializarNuevoJuego()

        } else inicializarNuevoJuego()

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val estadoTablero = this.tableroLogico.obtenerEstado()
        val estadoUpdater = this.updater.obtenerEstado()
        val estadoTimer = this.timer.getSegundosRestantes()
        this.timer.cancelarTemporizador()
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
            nombreJugadorData = this.nombreJugador,
            segundosRestantesData = estadoTimer,
            dimensionTableroData = this.dimensionTablero,
            juegoTerminadoData = this.juegoTerminado,
            tableroLogicoData = estadoTablero,
            updaterData = estadoUpdater,
            tableroVisualData = estadoTableroVisual
        )

        outState.putParcelable(KEY_JUEGO_ESTADO, estadoJuego)
    }

    private fun restaurarJuegoDesdeEstado(estadoJuego: JuegoEstado) {
        this.nombreJugador = estadoJuego.nombreJugadorData
        this.juegoTerminado = estadoJuego.juegoTerminadoData
        this.dimensionTablero = estadoJuego.dimensionTableroData
        val segundosRestantes = estadoJuego.segundosRestantesData

        setupTablero(segundosRestantes)
        this.tableroLogico.restaurarEstado(estadoJuego.tableroLogicoData)
        this.updater.restaurarEstado(estadoJuego.updaterData)

        crearTableroVisualDelEstado(estadoJuego.tableroVisualData)

    }

    private fun inicializarNuevoJuego() {
        this.juegoTerminado = false
        setupTablero(null)
        crearTableroVisual(this.dimensionTablero, this.dimensionTablero)
    }

    private fun inicializarVistas() {
        this.glTablero = findViewById<GridLayout>(R.id.glTablero)
        this.tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        this.tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        this.tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        this.tvMensajeJuego = findViewById<TextView>(R.id.tvMensajeJuego)
        this.tvNombreJugador = findViewById<TextView>(R.id.tvNombreJugador)
        this.tvTimer = findViewById<TextView>(R.id.tvTimer)

        this.btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        this.btnShowPopup = findViewById<ImageButton>(R.id.btnPopupMenu)
    }

    private fun setupTablero(segundosParaIniciarTimer: Int?) {
        val formato = this.getString(R.string.nombre_jugador)
        this.tvNombreJugador.text = String.format(formato, this.nombreJugador)
        this.tiempoConfigurado = getTiempoSegunDimension(this.dimensionTablero)
        this.timer = TimerManager(this, this)
        this.dialog = DialogManager(this, this)
        this.tableroLogico = TableroLogico(dimensionTablero, dimensionTablero)
        this.updater = UpdaterTextView(
            this,
            this.tvRestantes,
            this.tvMovimientos,
            this.tvAciertos,
            this.tvMensajeJuego,
            this.tableroLogico.getCantidadBarcos(),
            this
        )

        val tiempoInicial = segundosParaIniciarTimer ?: this.tiempoConfigurado
        this.timer.iniciarTemporizador(this.tiempoConfigurado, tiempoInicial)

    }

    private fun setupButtonListeners() {
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
        val tamanioEmoji = getTamanioEmojiSegunDimension(filas)

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

                    val fueAcierto = this.tableroLogico.tieneBarco(i, j)
                    boton.text = if (fueAcierto) emojiBarco else emojiAgua
                    this.tableroLogico.revelarBarco(i, j)
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

        val tamanioEmoji = getTamanioEmojiSegunDimension(this.dimensionTablero)

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
                        val fueAcierto = this.tableroLogico.tieneBarco(i, j)
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
                    val intent = Intent(this, HelpActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }
        }
        popup.show()
    }

    private fun compartirPuntuacion(puntuacion: Puntuacion) {
        val dimension = puntuacion.getDimensionTablero()
        val formato = this.getString(R.string.share_score_text_template)
        val texto = String.format(
            formato,
            puntuacion.getNombreJugador(),
            puntuacion.getAciertos(),
            puntuacion.getMovimientos(),
            dimension,
            dimension
        ).trimIndent()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, texto)
            type = "text/plain"
        }
        startActivity(
            Intent.createChooser(
                intent,
                this.getString(R.string.share_score_chooser_title)
            )
        )
    }

    private fun irALeaderboard(ultimaPuntuacion: Puntuacion) {
        val intent = Intent(this, LeaderboardActivity::class.java)
        intent.putExtra("NOMBRE_JUGADOR", ultimaPuntuacion.getNombreJugador())
        intent.putExtra("ACIERTOS", ultimaPuntuacion.getAciertos())
        intent.putExtra("MOVIMIENTOS", ultimaPuntuacion.getMovimientos())
        intent.putExtra("CANTIDAD_BARCOS", ultimaPuntuacion.getCantidadBarcos())
        intent.putExtra("DIMENSION_TABLERO", ultimaPuntuacion.getDimensionTablero())
        startActivity(intent)
    }

    // Implementaciones de la interfaz TimerInterface
    override fun onTickUpdateUI(segundos: Int) {
        val formato = this.getString(R.string.timer_segundos_restantes)
        this.tvTimer.text = String.format(formato, segundos)
    }

    // Implementaciones de la interfaz DialogListener
    override fun onDialogRestartGame() {
        onGameRestart()
    }

    override fun onDialogBackToHomeScreen() {
        finish()
    }

    override fun onDialogShowLeaderboard(puntuacion: Puntuacion) {
        irALeaderboard(puntuacion)
    }

    override fun onDialogShareScore(puntuacion: Puntuacion) {
        compartirPuntuacion(puntuacion)
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
        this.timer.cancelarTemporizador()
        deshabilitarTablero()
        if (LeaderboardManager.entraAlRanking(this, this.ultimaPuntuacion)) {
            guardarPuntuacion(this, this.ultimaPuntuacion)
            this.dialog.mostrarDialogoVictoriaLeaderboard(this.ultimaPuntuacion)
        } else {
            this.dialog.mostrarDialogoVictoria()
        }
    }

    override fun onGameRestart() {
        if (isFinishing || isDestroyed) return
        juegoTerminado = false
        this.timer.cancelarTemporizador()
        setupTablero(null)
        crearTableroVisual(this.dimensionTablero, this.dimensionTablero)
    }

    override fun onGameOver() {
        if (isFinishing || isDestroyed) return
        this.juegoTerminado = true
        this.timer.cancelarTemporizador()
        deshabilitarTablero()
        this.dialog.mostrarDialogoDerrota()
    }
}