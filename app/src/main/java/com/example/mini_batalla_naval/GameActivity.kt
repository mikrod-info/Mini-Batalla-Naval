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
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.widget.PopupMenu
import com.example.mini_batalla_naval.model.BotonVisualEstado
import com.example.mini_batalla_naval.model.JuegoEstado

//Grupo1: Kruk, Ivana y Rodriguez, Miguel
class GameActivity : AppCompatActivity(), GameEventListener {
    private lateinit var glTablero: GridLayout
    private lateinit var tableroLogico: Tablero
    private lateinit var updater: UpdaterTextView
    private lateinit var tvRestantes: TextView
    private lateinit var tvMovimientos: TextView
    private lateinit var tvAciertos: TextView
    private lateinit var tvMensajeJuego: TextView
    private lateinit var btnReiniciar: Button
    private lateinit var btnShowPopup: ImageButton
    private val KEY_JUEGO_ESTADO = "KEY_JUEGO_ESTADO"
    private var juegoTerminado: Boolean = false
    private var dimensionRecibida: Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        this.dimensionRecibida = intent.getIntExtra("DIMENSION_TABLERO", 6)

        if (savedInstanceState != null) {

            val estadoJuego: JuegoEstado? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getParcelable(KEY_JUEGO_ESTADO, JuegoEstado::class.java)
            } else {
                @Suppress("DEPRECATION") // Suprime la advertencia para versiones anteriores a API 33
                savedInstanceState.getParcelable<JuegoEstado>(KEY_JUEGO_ESTADO)
            }

            if (estadoJuego != null) {
                this.juegoTerminado = estadoJuego.juegoTerminadoData
                this.dimensionRecibida = estadoJuego.dimensionTableroData

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
            estadoTableroVisual.add(BotonVisualEstado(botonView.isEnabled, botonView.text.toString()))

        }

        val estadoJuego = JuegoEstado(
            dimensionTableroData = this.dimensionRecibida,
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
    }

    private fun inicializarVistas() {
        this.glTablero = findViewById<GridLayout>(R.id.glTablero)
        this.tvRestantes = findViewById<TextView>(R.id.tvRestantes)
        this.tvMovimientos = findViewById<TextView>(R.id.tvMovimientos)
        this.tvAciertos = findViewById<TextView>(R.id.tvAciertos)
        this.tvMensajeJuego = findViewById<TextView>(R.id.tvMensajeJuego)
        this.btnReiniciar = findViewById<Button>(R.id.btnReiniciar)
        this.btnShowPopup = findViewById<ImageButton>(R.id.btnMainHelp)
    }

    private fun setupTablero() {
        this.tableroLogico = Tablero(dimensionRecibida, dimensionRecibida)
        this.updater = UpdaterTextView(
            this,
            this.tvRestantes,
            this.tvMovimientos,
            this.tvAciertos,
            this.tvMensajeJuego,
            this.tableroLogico.getCantidadBarcos(),
            this
        )

        crearTableroVisual(dimensionRecibida, dimensionRecibida)
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
                    if (fueAcierto) this.tableroLogico.revelarBarco(i,j)
                    updater.registrarActividad(fueAcierto)
                    it.isEnabled = false
                }

                this.glTablero.addView(boton)
            }
        }
    }

    private fun crearTableroVisualDelEstado(estadoBotones: ArrayList<BotonVisualEstado>) {
        this.glTablero.removeAllViews()
        this.glTablero.rowCount = this.dimensionRecibida
        this.glTablero.columnCount = this.dimensionRecibida

        val tamanioEmoji = when (this.dimensionRecibida) {
            6 -> 24f
            8 -> 20f
            10 -> 16f
            else -> 24f
        }

        var estadoIndex = 0
        for (i in 0 until this.dimensionRecibida) {
            for (j in 0 until this.dimensionRecibida) {
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

                    val fueAcierto = this.tableroLogico.fueAcierto(i, j)
                    if (it.isEnabled) {
                        val esAcierto = this.tableroLogico.fueAcierto(i,j)
                        boton.text = if (esAcierto) emojiBarco else emojiAgua
                        updater.registrarActividad(esAcierto)
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
                    // Acción para "Inicio": Volver a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
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

    override fun onGameWon() {
        this.juegoTerminado = true
        deshabilitarTablero()
    }

    override fun onGameRestart() {
        this.juegoTerminado = false
        setupTablero()
    }
}
