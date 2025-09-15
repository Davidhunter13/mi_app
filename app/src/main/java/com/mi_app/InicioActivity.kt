package com.mi_app

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        // Configurar el Drawer
        setupDrawer(
            findViewById(R.id.drawerLayout),
            findViewById(R.id.navView)
        )

        val nombre = intent.getStringExtra("nombre")
        val correo = intent.getStringExtra("correo")

        findViewById<TextView>(R.id.tvNombre).text = nombre
        findViewById<TextView>(R.id.tvCorreo).text = correo
    }
}
