package com.mi_app

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Surface
import android.view.TextureView
import android.widget.*
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class VideoActivity : BaseActivity(), TextureView.SurfaceTextureListener {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var textureView: TextureView
    private lateinit var seekBar: SeekBar
    private lateinit var btnPlayPause: ImageButton
    private lateinit var btnFaster: ImageButton
    private lateinit var btnSlower: ImageButton

    private var isPlaying = false
    private var playbackSpeed = 1.0f
    private val handler = Handler(Looper.getMainLooper())
    private var simulateSpeed = false
    private var lastUpdateTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

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

        textureView = findViewById(R.id.videoView)
        textureView.surfaceTextureListener = this

        seekBar = findViewById(R.id.seekBar)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnFaster = findViewById(R.id.btnFaster)
        btnSlower = findViewById(R.id.btnSlower)

        // Detectar si debemos simular velocidad en versiones < M
        simulateSpeed = Build.VERSION.SDK_INT < Build.VERSION_CODES.M

        setupControls()
    }

    private fun setupControls() {
        btnPlayPause.setOnClickListener {
            if (isPlaying) {
                mediaPlayer?.pause()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play)
            } else {
                mediaPlayer?.start()
                btnPlayPause.setImageResource(android.R.drawable.ic_media_pause)
                updateSeekBar()
            }
            isPlaying = !isPlaying
        }

        btnFaster.setOnClickListener {
            if (playbackSpeed < 2.0f) {
                playbackSpeed += 0.25f
                applyPlaybackSpeed()
                Toast.makeText(this, "Velocidad: x$playbackSpeed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "⚠️ Velocidad máxima alcanzada", Toast.LENGTH_SHORT).show()
            }
        }

        btnSlower.setOnClickListener {
            if (playbackSpeed > 0.5f) {
                playbackSpeed -= 0.25f
                applyPlaybackSpeed()
                Toast.makeText(this, "Velocidad: x$playbackSpeed", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "⚠️ Velocidad mínima alcanzada", Toast.LENGTH_SHORT).show()
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mediaPlayer?.seekTo(progress)
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        })
    }

    private fun applyPlaybackSpeed() {
        if (!simulateSpeed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mediaPlayer?.let {
                    val params = it.playbackParams ?: PlaybackParams()
                    params.speed = playbackSpeed
                    it.playbackParams = params
                }
            }
        }
        // En simulateSpeed, la velocidad se ajusta manualmente en updateSeekBar
    }

    private fun updateSeekBar() {
        mediaPlayer?.let { mp ->
            seekBar.max = mp.duration
            lastUpdateTime = System.currentTimeMillis()
            handler.post(object : Runnable {
                override fun run() {
                    if (mp.isPlaying) {
                        val currentTime = System.currentTimeMillis()
                        val delta = (currentTime - lastUpdateTime).toFloat()
                        lastUpdateTime = currentTime

                        if (simulateSpeed) {
                            var newPos = (seekBar.progress + delta * playbackSpeed).toInt()
                            if (newPos < seekBar.max) {
                                seekBar.progress = newPos
                            } else {
                                newPos = 0
                                seekBar.progress = 0
                                mp.seekTo(0)
                            }
                        } else {
                            seekBar.progress = mp.currentPosition
                        }
                        handler.postDelayed(this, 100)
                    }
                }
            })
        }
    }

    // 🔹 SurfaceTextureListener
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        val videoResId = resources.getIdentifier("video_demo2", "raw", packageName)
        if (videoResId == 0) {
            Toast.makeText(this, "❌ No se encontró video_demo2.mp4 en /res/raw", Toast.LENGTH_LONG).show()
            return
        }
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@VideoActivity, Uri.parse("android.resource://$packageName/$videoResId"))
            setSurface(Surface(surface))
            isLooping = true
            setOnPreparedListener {
                it.start()
                this@VideoActivity.isPlaying = true
                updateSeekBar()
                Toast.makeText(this@VideoActivity, "✅ Video cargado correctamente", Toast.LENGTH_SHORT).show()
            }
            setOnErrorListener { _, what, extra ->
                Toast.makeText(this@VideoActivity, "❌ Error al reproducir video (what=$what, extra=$extra)", Toast.LENGTH_LONG).show()
                true
            }
            prepareAsync()
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        mediaPlayer?.release()
        mediaPlayer = null
        return true
    }
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
