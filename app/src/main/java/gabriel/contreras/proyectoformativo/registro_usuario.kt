package gabriel.contreras.proyectoformativo

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import Modelo.ClaseConexion
import android.content.Intent
import android.widget.Toast


class registro_usuario : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_usuario)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val txtUsuarioRegistrarse = findViewById<EditText>(R.id.txtUsuarioRegistro)
        val txtContrasenaRegistrarse = findViewById<EditText>(R.id.txtContrasenaRegistro)
        val btnRegistrarse = findViewById<ImageView>(R.id.btnCrearCuenta)
        val btncerrar = findViewById<ImageView>(R.id.btnSalirRegistroUsuario)

        btncerrar.setOnClickListener {
            val login = Intent(this, login::class.java)
            startActivity(login)
            finish()
        }
        btnRegistrarse.setOnClickListener {
            val usuario = txtUsuarioRegistrarse.text.toString()
            val contrasena = txtContrasenaRegistrarse.text.toString()

            if (usuario.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(
                    this@registro_usuario,
                    "Por favor, llenar los espacios obligatorios",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                            val crearUsuario = objConexion?.prepareStatement("INSERT INTO Enfermera(usuario, contrasena) VALUES (?, ?) ")!!
                            crearUsuario.setString(1, usuario)
                            crearUsuario.setString(2, contrasena)
                            crearUsuario.executeUpdate()
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@registro_usuario, "Usuario creado, regresa a la pantalla anterior para iniciar sesión.", Toast.LENGTH_LONG).show()
                                txtUsuarioRegistrarse.setText("")
                                txtContrasenaRegistrarse.setText("")
                            }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@registro_usuario, "Ocurrió un error al crear la cuenta. Por favor, intente nuevamente.", Toast.LENGTH_SHORT).show()
                            println("Error: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}