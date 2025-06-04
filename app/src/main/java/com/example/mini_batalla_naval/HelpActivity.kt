package com.example.mini_batalla_naval // Asegúrate que este sea tu paquete correcto

import android.os.Bundle
import android.widget.Button // IMPORTANTE: Añade esta importación para Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_help)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val buttonBack: Button = findViewById(R.id.buttonHelpBack)

        // 2. Configura un listener para que reaccione a los clics
        buttonBack.setOnClickListener {
            // Esta acción finaliza (cierra) la actividad actual (HelpActivity)
            // y vuelve a la actividad anterior desde la que fue llamada.
            finish()
        }

    }
}