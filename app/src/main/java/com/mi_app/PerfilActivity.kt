package com.mi_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class PerfilActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // 📦 Datos recibidos desde el intent
        val nombre = intent.getStringExtra("nombre") ?: "Usuario"
        val correo = intent.getStringExtra("correo") ?: "Sin correo"
        val carpeta = intent.getStringExtra("carpeta") ?: ""

        // 🔹 Configuración del menú lateral
        setupDrawer(
            findViewById(R.id.drawerLayout),
            findViewById(R.id.navView),
            nombre,
            carpeta,
            correo
        )

        // 🔹 Muestra versión de la app en el header (opcional)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        datosHeader(nombre, carpeta, versionName)

        // 🧠 Referencias a vistas
        val fotoPerfil = findViewById<ImageView>(R.id.fotoPerfil)
        val tvNombre = findViewById<TextView>(R.id.tvNombre)
        val tvCorreo = findViewById<TextView>(R.id.tvCorreo)
        val tvTelefono = findViewById<TextView>(R.id.tvTelefono)
        val tvDocumento = findViewById<TextView>(R.id.tvDocumento)
        val tvFechaNacimiento = findViewById<TextView>(R.id.tvFechaNacimiento)

        // 🧩 Asigna valores dinámicos
        tvNombre.text = nombre
        tvCorreo.text = "Correo electrónico: $correo"
        tvTelefono.text = "Teléfono: 320398531"
        tvDocumento.text = "Documento/ID: 1014298107"
        tvFechaNacimiento.text = "Fecha de nacimiento: 13 junio 1998"

        // 🖼️ Cargar imagen desde drawable o desde carpeta interna
        try {
            if (carpeta.isNotEmpty()) {
                val resId = resources.getIdentifier(carpeta, "drawable", packageName)
                if (resId != 0) {
                    fotoPerfil.setImageResource(resId)
                } else {
                    fotoPerfil.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_perfil))
                }
            } else {
                fotoPerfil.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_perfil))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fotoPerfil.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_perfil))
        }
    }
}
