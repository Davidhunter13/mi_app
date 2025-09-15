package com.mi_app

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
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

    fun setupDrawer(drawerLayout: DrawerLayout, navView: NavigationView) {
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

        // Items del menú
        navView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.navInicio -> startActivity(Intent(this, InicioActivity::class.java))
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
