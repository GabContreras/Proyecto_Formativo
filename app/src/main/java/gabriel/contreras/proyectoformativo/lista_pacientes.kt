package gabriel.contreras.proyectoformativo

import Modelo.ClaseConexion
import Modelo.Paciente
import RecyclerViewHelper.AdaptadorPaciente
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class lista_pacientes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val rcvPaciente = findViewById<RecyclerView>(R.id.rcvPacientes)

        val btnAgregarPaciente = findViewById<ImageButton>(R.id.btnAgregarPaciente)

        rcvPaciente.layoutManager = LinearLayoutManager(this)

        fun obtenerPacientes(): List<Paciente> {
            val listadoPaciente = mutableListOf<Paciente>()

            // Crear un objeto de la clase conexión
            val objConexion = ClaseConexion().cadenaConexion()

            //2 Crear un statement
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Paciente")!!
            // Recorrer todos los datos que me trajo el select

            while (resultSet.next()) {
                val id_paciente= resultSet.getInt("id_paciente")
                val nombre= resultSet.getString("nombre")
                val apellido = resultSet.getString("apellido")
                val edad = resultSet.getString("edad")
                val enfermedad= resultSet.getString("enfermedad")
                val fecha_de_ingreso = resultSet.getString("fecha_de_ingreso")

                val valoresJuntos = Paciente(
                    id_paciente,
                    nombre,
                    apellido,
                    edad,
                    enfermedad,
                    fecha_de_ingreso
                    )
                listadoPaciente.add(valoresJuntos)
            }
            return listadoPaciente

        }

        // Ejecutamos la función
        CoroutineScope(Dispatchers.IO).launch {
            val pacienteDb = obtenerPacientes()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorPaciente(pacienteDb)
                rcvPaciente.adapter = adapter
            }
        }


        btnAgregarPaciente.setOnClickListener {
            //Cambio de pantalla para poder registrarse
            val pantallaRegistroPacientes = Intent(this, registro_pacientes::class.java)
            startActivity(pantallaRegistroPacientes)
        }
    }
}