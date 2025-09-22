package com.mi_app

import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

// Clase de usuario simple
data class Usuario(val nombre: String, val correo: String, val contrasena: String , val carpeta: String)

class Login : AppCompatActivity() {

    // Base de datos en memoria (array)
    private val usuarios = arrayListOf(
        Usuario("David Ronaldo Parra Buitrago", "david@gmail.com", "1234" ,"david"),
        Usuario("lorena", "lorena@gmail.com", "abcd" , "lorena"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Ajuste de paddings
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etCorreo = findViewById<TextInputEditText>(R.id.etCorreo)
        val etContrasena = findViewById<TextInputEditText>(R.id.etContrasena)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        // ✅ Valores predeterminados
        etCorreo.setText("david@gmail.com")
        etContrasena.setText("1234")

        btnLogin.setOnClickListener {
            val correo = etCorreo.text.toString().trim()
            val contrasena = etContrasena.text.toString().trim()

            if (correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                Toast.makeText(this, "Ingresa un correo válido", Toast.LENGTH_SHORT).show()
            } else {
                // Buscar usuario en el array
                val usuario = usuarios.find { it.correo == correo && it.contrasena == contrasena }

                if (usuario != null) {
                    Toast.makeText(this, "Bienvenido ${usuario.nombre}", Toast.LENGTH_SHORT).show()

                    // Abrir otra Activity y pasar los datos
                    val intent = android.content.Intent(this, InicioActivity::class.java)
                    intent.putExtra("nombre", usuario.nombre)
                    intent.putExtra("correo", usuario.correo)
                    intent.putExtra("carpeta", usuario.carpeta)
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}
