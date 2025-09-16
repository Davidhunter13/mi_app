package com.mi_app

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mi_app.InicioActivity.CarruselAdapter


class ImagenesActivity : BaseActivity() {

    private lateinit var carruselRecycler: RecyclerView
    private lateinit var adapter: ImagenesActivity.CarruselAdapter
    private val handler = Handler(Looper.getMainLooper())
    private var currentIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imagenes)

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


        //  Configurar el carrusel
        carruselRecycler = findViewById(R.id.carruselRecycler)
        carruselRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val imagenes = (1..27).map { i ->
            resources.getIdentifier("imagen_$i", "drawable", packageName)
        }.filter { it != 0 } // filtra los que existan

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


        val thumbnailRecycler = findViewById<RecyclerView>(R.id.thumbnailRecycler)
        thumbnailRecycler.layoutManager = GridLayoutManager(this, 2) // 2 columnas
        thumbnailRecycler.adapter = ThumbnailAdapter(listaImagenes) { imagen ->
            mostrarDetalle(this, imagen)
        }


    }

    private fun startAutoScroll() {
        val runnable = object : Runnable {
            override fun run() {
                if (adapter.itemCount == 0) return

                carruselRecycler.smoothScrollToPosition(currentIndex)
                adapter.updateIndicators(currentIndex) // <-- actualizar indicadores
                currentIndex = (currentIndex + 1) % adapter.itemCount

                handler.postDelayed(this, 3000)
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
                        val size = 8.toDp(context)
                        val params = LinearLayout.LayoutParams(size, size)
                        params.setMargins(2.toDp(context), 0, 2.toDp(context), 0)
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

    class Imagen(
        val id: Int,
        val nombre: String,
        val descripcion: String,
        val recurso: Int // R.drawable.imagenX
    )

    val listaImagenes = listOf(
        Imagen(
            1,
            "Junior",
            "Junior, el intrépido guardián de la noche, recorre los senderos envuelto en misterio. Sus ojos reflejan la luz de las estrellas y su espíritu valiente enfrenta cualquier sombra que se cruce en su camino, protegiendo a quienes ama con una lealtad inquebrantable.",
            R.drawable.img1
        ),
        Imagen(
            2,
            "Dante",
            "Dante, señor de las montañas cubiertas de nieve, avanza con paso firme entre los picos más altos. Su mirada poderosa y decidida parece leer los secretos del viento, y su presencia impone respeto a todos los que osan acercarse a su territorio helado.",
            R.drawable.img2
        ),
        Imagen(
            3,
            "Tommy",
            "Tommy, explorador de la ciudad al caer el sol, domina los tejados y callejones con una gracia única. Su espíritu libre danza entre luces y sombras, y su inteligencia estratégica lo convierte en un aventurero silencioso que siempre deja su marca en la urbe dorada por el atardecer.",
            R.drawable.img3
        ),
        Imagen(
            4,
            "Maya",
            "Maya, fuerza indomable y belleza salvaje, emerge con la majestuosidad de un volcán en erupción. Cada movimiento suyo irradia poder y libertad, y su corazón late al ritmo de la tierra, dejando huella en todos los que contemplan su espíritu ardiente y valiente.",
            R.drawable.img4
        )
    )

    class ThumbnailAdapter(
        private val lista: List<Imagen>,
        private val clickListener: (Imagen) -> Unit
    ) : RecyclerView.Adapter<ThumbnailAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imagen: ImageView = view.findViewById(R.id.imgThumbnail)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_thumbnail, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount() = lista.size

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = lista[position]
            holder.imagen.setImageResource(item.recurso)
            holder.itemView.setOnClickListener { clickListener(item) }
        }
    }

    fun mostrarDetalle(context: Context, imagen: Imagen) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_imagen)
        val imgView = dialog.findViewById<ImageView>(R.id.imgDetalle)
        val tvNombre = dialog.findViewById<TextView>(R.id.tvNombre)
        val tvDescripcion = dialog.findViewById<TextView>(R.id.tvDescripcion)

        imgView.setImageResource(imagen.recurso)
        tvNombre.text = imagen.nombre
        tvDescripcion.text = imagen.descripcion

        dialog.show()
    }



}
