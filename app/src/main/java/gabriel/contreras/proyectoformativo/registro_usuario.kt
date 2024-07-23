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



        btnRegistrarse.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {

                val objConexion = ClaseConexion().cadenaConexion()


                val crearUsuario =
                    objConexion?.prepareStatement("INSERT INTO Enfermera(usuario, contrasena) VALUES (?, ?) ")!!
                crearUsuario.setString(2, txtUsuarioRegistrarse.text.toString())
                crearUsuario.setString(3, txtContrasenaRegistrarse.text.toString())
                crearUsuario.executeUpdate()
                withContext(Dispatchers.Main) {

                    Toast.makeText(this@registro_usuario, "Usuario creado, Regresa a la pantalla anterior para poder iniciar sesión en el sistema", Toast.LENGTH_LONG).show()
                    txtUsuarioRegistrarse.setText("")
                    txtContrasenaRegistrarse.setText("")
                }
            }

        }


    }
}