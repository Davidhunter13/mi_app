package com.mi_app

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import androidx.core.content.ContextCompat

class InicioActivity : BaseActivity() {
    private lateinit var carruselRecycler: RecyclerView
    private lateinit var adapter: CarruselAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0

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
        datosHeader(nombre, carpeta, versionName)

        findViewById<TextView>(R.id.tvNombre).text = nombre

        // Foto de perfil
        val ivFotoPerfil = findViewById<ImageView>(R.id.ivFotoPerfilHome)
        val resId = carpeta?.let { resources.getIdentifier(it, "drawable", packageName) }

        if (resId != null && resId != 0) {
            ivFotoPerfil.setImageResource(resId)
        } else {
            ivFotoPerfil.setImageResource(R.drawable.ic_perfil)
        }

        // 🚀 Configurar el carrusel
        carruselRecycler = findViewById(R.id.carruselRecycler)
        carruselRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val imagenes = listOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3
        )

        adapter = CarruselAdapter(imagenes)
        carruselRecycler.adapter = adapter

        // Iniciar el autoscroll
        startAutoScroll()
    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (adapter.itemCount == 0) return

                carruselRecycler.smoothScrollToPosition(currentIndex)
                currentIndex = (currentIndex + 1) % adapter.itemCount

                handler.postDelayed(this, 5000)
            }
        }
        handler.post(runnable)
    }

    class CarruselAdapter(private val images: List<Int>) :
        RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder>() {

        inner class CarruselViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarruselViewHolder {
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply {
                    setMargins(10, 10, 10, 10)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                background =
                    ContextCompat.getDrawable(parent.context, R.drawable.bg_redondeado_tran)
                clipToOutline = true
            }
            return CarruselViewHolder(imageView)
        }

        override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
        }

        override fun getItemCount(): Int = images.size
    }
}
