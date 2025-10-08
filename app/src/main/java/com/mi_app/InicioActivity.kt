package com.mi_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.LinearLayout
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
            nombre,
            carpeta,
            correo

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

        //  Configurar el carrusel
            carruselRecycler = findViewById(R.id.carruselRecycler)
        carruselRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val imagenes = listOf(
            R.drawable.img1,
            R.drawable.img2,
            R.drawable.img3,
            R.drawable.img4
        )

        // Obtener layout de indicadores
        val indicatorLayout = findViewById<LinearLayout>(R.id.indicatorLayout)

        // Pasar indicador al Adapter
        adapter = CarruselAdapter(imagenes, indicatorLayout)
        carruselRecycler.adapter = adapter

        // Inicializar indicadores una sola vez
        adapter.setupIndicators(this)

        // Iniciar el autoscroll
        startAutoScroll()

        carruselRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // Obtener la posición del item visible al centro
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                if (visiblePosition != RecyclerView.NO_POSITION) {
                    adapter.updateIndicators(visiblePosition)
                    currentIndex = visiblePosition
                }
            }
        })


        // 👉 Imagenes
        findViewById<LinearLayout>(R.id.navFotosHome).setOnClickListener {
            val intent = Intent(this, ImagenesActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("correo", correo)
                putExtra("carpeta", carpeta)
            }
            startActivity(intent)
        }

        // 👉 Web
        findViewById<LinearLayout>(R.id.navWebHome).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("correo", correo)
                putExtra("carpeta", carpeta)
            }
            startActivity(intent)
        }

        // 👉 Videos
        findViewById<LinearLayout>(R.id.navVideosHome).setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("correo", correo)
                putExtra("carpeta", carpeta)
            }
            startActivity(intent)
        }

        // 👉 perfil
        findViewById<LinearLayout>(R.id.navPerfilHome).setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("correo", correo)
                putExtra("carpeta", carpeta)
            }
            startActivity(intent)
        }

        // 👉 perfil
        findViewById<LinearLayout>(R.id.navBotonesHome).setOnClickListener {
            val intent = Intent(this, BotonesActivity::class.java).apply {
                putExtra("nombre", nombre)
                putExtra("correo", correo)
                putExtra("carpeta", carpeta)
            }
            startActivity(intent)
        }

    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (adapter.itemCount == 0) return

                carruselRecycler.smoothScrollToPosition(currentIndex)
                adapter.updateIndicators(currentIndex)
                currentIndex = (currentIndex + 1) % adapter.itemCount

                handler.postDelayed(this, 5000)
            }
        }
        handler.post(runnable)
    }


    class CarruselAdapter(
        private val images: List<Int>,
        private val indicatorLayout: LinearLayout
    ) : RecyclerView.Adapter<CarruselAdapter.CarruselViewHolder>() {

        inner class CarruselViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarruselViewHolder {
            val imageView = ImageView(parent.context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                ).apply { setMargins(10, 0, 10, 0) } // opcional
                scaleType = ImageView.ScaleType.FIT_CENTER
                background = ContextCompat.getDrawable(parent.context, R.drawable.bg_redondeado_tran)
                clipToOutline = true
            }
            return CarruselViewHolder(imageView)
        }


        override fun onBindViewHolder(holder: CarruselViewHolder, position: Int) {
            holder.imageView.setImageResource(images[position])
            updateIndicators(position) // Solo actualizar indicador
        }

        override fun getItemCount(): Int = images.size

        private fun Int.toDp(context: Context): Int =
            (this * context.resources.displayMetrics.density).toInt()

        /** Inicializa los puntos de indicador */
        fun setupIndicators(context: Context) {
            if (indicatorLayout.childCount == 0) {
                for (i in images.indices) {
                    val dot = ImageView(context).apply {
                        setImageResource(R.drawable.dot_inactive)
                        val size = 16.toDp(context)
                        val params = LinearLayout.LayoutParams(size, size)
                        params.setMargins(8.toDp(context), 0, 8.toDp(context), 0)
                        layoutParams = params
                    }
                    indicatorLayout.addView(dot)
                }
            }
        }

        /** Actualiza el punto activo */
        fun updateIndicators(activePosition: Int) {
            for (i in 0 until indicatorLayout.childCount) {
                val dot = indicatorLayout.getChildAt(i) as ImageView
                dot.setImageResource(if (i == activePosition) R.drawable.dot_active else R.drawable.dot_inactive)
            }
        }
    }


}
