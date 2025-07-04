package com.example.mini_batalla_naval.model

import android.os.CountDownTimer

class TimerManager(
    private val gameEventListener: GameEventListener,
    private var timerInterface: TimerInterface
) {
    private var countDownTimer: CountDownTimer? = null
    private var segundosRestantes: Int = 0
    private var segundosTotalesConfigurado: Int = 0

    fun iniciarTemporizador(totalSegundosConfig: Int, segundosActuales: Int? = null) {
        this.segundosTotalesConfigurado = totalSegundosConfig
        this.segundosRestantes = segundosActuales ?: totalSegundosConfig

        this.countDownTimer?.cancel()

        this.countDownTimer = object : CountDownTimer(this.segundosRestantes * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                this@TimerManager.segundosRestantes = (millisUntilFinished / 1000).toInt()
                timerInterface.onTickUpdateUI(this@TimerManager.segundosRestantes)
            }

            override fun onFinish() {
                this@TimerManager.segundosRestantes = 0
                timerInterface.onTickUpdateUI(this@TimerManager.segundosRestantes)
                gameEventListener.onGameOver()
            }
        }.start()
    }

    fun getSegundosRestantes(): Int {
        return this.segundosRestantes
    }

    fun cancelarTemporizador() {
        this.countDownTimer?.cancel()
        this.countDownTimer = null
    }
}