package gabriel.contreras.proyectoformativo

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import kotlinx.coroutines.launch

import androidx.core.view.WindowInsetsCompat
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


        //Botones para ingresar al sistema

        btnIngresar.setOnClickListener {
            val pantallaPrincipal = Intent(this, lista_pacientes::class.java)

            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()


                val comprobacionUsuario = objConexion?.prepareStatement("SELECT * FROM TB_USUARIO WHERE NOMBRE_DE_USUARIO = ? AND CONTRASENA = ?")!!
                comprobacionUsuario.setString(1, txtUsuario.text.toString())
                comprobacionUsuario.setString(2, txtContrasena.text.toString())
                val resultado = comprobacionUsuario.executeQuery()

                if (resultado.next()) {
                    startActivity(pantallaPrincipal)
                } else {
                    println("Usuario no encontrado, verifique las credenciales")
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