package com.mi_app

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InicioActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val nombre = intent.getStringExtra("nombre")
        val correo = intent.getStringExtra("correo")
        val carpeta = intent.getStringExtra("carpeta")

        // Configurar el Drawer
        setupDrawer(
            findViewById(R.id.drawerLayout),
            findViewById(R.id.navView),
        )
        // Configurar datos del header
        val versionName = packageManager
            .getPackageInfo(packageName, 0).versionName
        datosHeader(nombre,carpeta, versionName)

        findViewById<TextView>(R.id.tvNombre).text = nombre

        // Foto de perfil
        val ivFotoPerfil = findViewById<ImageView>(R.id.ivFotoPerfilHome)

        // Intentar cargar la foto desde drawable con el prefijo "carpeta"
        val resId = carpeta?.let { resources.getIdentifier("${it}", "drawable", packageName) }

        if (resId != null && resId != 0) {
            ivFotoPerfil.setImageResource(resId)
        } else {
            // Si no existe la imagen, usar un recurso por defecto
            ivFotoPerfil.setImageResource(R.drawable.ic_perfil)
        }

    }
}
