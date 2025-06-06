package com.example.mini_batalla_naval // Asegúrate que este sea tu paquete correcto

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)

        val buttonBack: Button = findViewById(R.id.btnHelpBack)
        buttonBack.setOnClickListener {
            // Esta acción finaliza (cierra) la actividad actual (HelpActivity)
            // y vuelve a la actividad anterior desde la que fue llamada.
            finish()
        }

    }
}