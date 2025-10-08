package com.mi_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.TextureView


class BotonesActivity : BaseActivity() {

    private var playbackSpeed = 1.0f
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botones)

        val nombre = intent.getStringExtra("nombre")
        val correo = intent.getStringExtra("correo")
        val carpeta = intent.getStringExtra("carpeta")

        setupDrawer(
            findViewById(R.id.drawerLayout),
            findViewById(R.id.navView),
            nombre,
            carpeta,
            correo
        )

        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        datosHeader(nombre, carpeta, versionName)
    }
}
