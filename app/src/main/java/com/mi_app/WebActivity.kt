package com.mi_app

import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

class WebActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val nombre = intent.getStringExtra("nombre")
        val correo = intent.getStringExtra("correo")
        val carpeta = intent.getStringExtra("carpeta")

        // Configurar el Drawer
        setupDrawer(
            findViewById(R.id.drawerLayout),
            findViewById(R.id.navView),
            nombre,
            carpeta,
            correo
        )

        // Configurar datos del header
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        datosHeader(nombre, carpeta, versionName)

        // Obtener referencias
        val urlInput = findViewById<TextInputEditText>(R.id.urlWeb)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val webView = findViewById<WebView>(R.id.webContainer)

        // Configurar WebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }

        // Asegurar que los links se abran dentro de WebView
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }
        }

        // Acción del botón Buscar
        btnBuscar.setOnClickListener {
            val url = urlInput.text.toString().trim()
            if (url.isNotEmpty()) {
                val finalUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    "https://$url"
                } else {
                    url
                }
                webView.loadUrl(finalUrl)
            } else {
                Toast.makeText(this, "Ingresa una URL válida", Toast.LENGTH_SHORT).show()
            }
        }

        // Opcional: cargar URL por defecto
        val defaultUrl = "https://www.poli.edu.co/"
        urlInput.setText(defaultUrl)
        webView.loadUrl(defaultUrl)
    }
}
