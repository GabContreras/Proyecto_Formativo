package gabriel.contreras.proyectoformativo

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope

class login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtUsuario = findViewById<EditText>(R.id.txtUsuarioRegistro)
        val txtContrasena = findViewById<EditText>(R.id.txtContrasenaRegistro)
        val btnIngresar = findViewById<ImageView>(R.id.btnIngresar)
        val txtRegistrare = findViewById<TextView>(R.id.btnRegistrarse)

        btnIngresar.setOnClickListener {
            val usuario = txtUsuario.text.toString().trim()
            val contrasena = txtContrasena.text.toString().trim()

            // Validación de campos vacíos
            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(
                    this, "Por favor, ingrese su usuario y contraseña.", Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                        val comprobacionUsuario =
                            objConexion?.prepareStatement("SELECT * FROM Enfermera WHERE usuario = ? AND contrasena = ?")!!
                        comprobacionUsuario.setString(1, usuario)
                        comprobacionUsuario.setString(2, contrasena)
                        val resultado = comprobacionUsuario.executeQuery()

                        if (resultado.next()) {
                            withContext(Dispatchers.Main) {
                                val pantallaPrincipal =
                                    Intent(this@login, lista_pacientes::class.java)
                                startActivity(pantallaPrincipal)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    this@login,
                                    "Usuario no encontrado, verifique las credenciales.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@login, "Error: ${e.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }

                }
            }
        }

        txtRegistrare.setOnClickListener {
            //Cambio de pantalla para poder registrarse
            val pantallaRegistrarse = Intent(this, registro_usuario::class.java)
            startActivity(pantallaRegistrarse)
        }

    }
}