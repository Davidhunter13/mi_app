package com.mi_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // El layout específico de la actividad se inflará luego
    }

    fun setupDrawer(drawerLayout: DrawerLayout, navView: NavigationView ,nombre_persona: String? = null, nombre_foto: String? = null ,correo_persona: String? = null) {
        this.drawerLayout = drawerLayout
        this.navView = navView

        // Botón hamburguesa
        val btnHamburguesa = findViewById<ImageButton>(R.id.btnHamburguesa)
        btnHamburguesa.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Botón cerrar en header
        val btnCerrar = navView.getHeaderView(0).findViewById<ImageButton>(R.id.btnCerrarDrawer)
        btnCerrar.setOnClickListener {
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navInicio -> {
                    if (this !is InicioActivity) {
                        val intent = Intent(this, InicioActivity::class.java).apply {
                            putExtra("nombre", nombre_persona)
                            putExtra("correo", correo_persona)
                            putExtra("carpeta", nombre_foto)
                        }
                        startActivity(intent)
                    }
                }

                R.id.navWeb -> {
                    if (this !is WebActivity) {
                        val intent = Intent(this, WebActivity::class.java).apply {
                            putExtra("nombre", nombre_persona)
                            putExtra("correo", correo_persona)
                            putExtra("carpeta", nombre_foto)
                        }
                        startActivity(intent)
                    }
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }


        val navView = findViewById<NavigationView>(R.id.navView)

        // Hacer que el NavigationView respete notch y botones de navegación
        navView.setOnApplyWindowInsetsListener { view, insets ->
            view.setPadding(
                view.paddingLeft,
                insets.systemWindowInsetTop,    // margen arriba (barra de estado / notch)
                view.paddingRight,
                insets.systemWindowInsetBottom  // margen abajo (botones de navegación)
            )
            insets.consumeSystemWindowInsets()
        }


    }


    fun datosHeader(nombre: String? = null, nombre_foto: String? = null, version: String? = null) {
        val navView = findViewById<NavigationView>(R.id.navView)
        // Header: obtener la vista del header
        val headerView = navView.getHeaderView(0)

        // Nombre de usuario
        val tvNombreUsuario = headerView.findViewById<TextView>(R.id.tvNombreUsuario)
        tvNombreUsuario.text = nombre ?: "Usuario"

        // Foto de perfil
        val ivFotoPerfil = headerView.findViewById<ImageView>(R.id.ivFotoPerfil)

        // Intentar cargar la foto desde drawable con el prefijo "carpeta"
        val resId = nombre_foto?.let { resources.getIdentifier("${it}", "drawable", packageName) }

        if (resId != null && resId != 0) {
            ivFotoPerfil.setImageResource(resId)
        } else {
            // Si no existe la imagen, usar un recurso por defecto
            ivFotoPerfil.setImageResource(R.drawable.ic_perfil)
        }

        // Footer: versión dinámica
        val tvVersionFooter = navView.findViewById<TextView>(R.id.tvVersionFooter)
        tvVersionFooter.text = "Versión - ${version ?: "?"}"
    }


}
