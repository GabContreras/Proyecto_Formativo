package gabriel.contreras.proyectoformativo

import Modelo.ClaseConexion
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class editar_paciente : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_paciente)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener los datos del Intent
        val idPaciente = intent.getIntExtra("id_paciente", -1)
        val nombrePaciente = intent.getStringExtra("nombre") ?: ""
        val apellidoPaciente = intent.getStringExtra("apellido") ?: ""
        val edadPaciente = intent.getStringExtra("edad") ?: ""
        val enfermedadPaciente = intent.getStringExtra("enfermedad") ?: ""
        val fechaIngresoPaciente = intent.getStringExtra("fecha_de_ingreso") ?: ""

        // Referenciar los EditText
        val txtNombrePaciente = findViewById<EditText>(R.id.txtNombrePacienteEditar)
        val txtApellidoPaciente = findViewById<EditText>(R.id.txtApellidoPacienteEditar)
        val txtEdadPaciente = findViewById<EditText>(R.id.txtEdadPacienteEditar)
        val txtEnfermedadPaciente = findViewById<EditText>(R.id.txtEnfermedadPacienteEditar)
        val txtFechaIngresoPaciente = findViewById<EditText>(R.id.txtFechaDeIngresoPacienteEditar)
        val btnEditar = findViewById<ImageButton>(R.id.btnRegistrarPacienteEditar)
        val btncerrar = findViewById<ImageView>(R.id.btnSalir)


        // Cargar los datos en los EditText
        txtNombrePaciente.setText(nombrePaciente)
        txtApellidoPaciente.setText(apellidoPaciente)
        txtEdadPaciente.setText(edadPaciente)
        txtEnfermedadPaciente.setText(enfermedadPaciente)
        txtFechaIngresoPaciente.setText(fechaIngresoPaciente)

        btncerrar.setOnClickListener {
            val pantallaPacientes = Intent(this, lista_pacientes::class.java)
            startActivity(pantallaPacientes)
            finish()
        }

        btnEditar.setOnClickListener {
            val nombrePaciente = txtNombrePaciente.text.toString()
            val apellidoPaciente = txtApellidoPaciente.text.toString()
            val edadPaciente = txtEdadPaciente.text.toString()
            val enfermedadPaciente = txtEnfermedadPaciente.text.toString()
            val fechaIngresoPaciente = txtFechaIngresoPaciente.text.toString()

            if (nombrePaciente.isEmpty() || apellidoPaciente.isEmpty() || edadPaciente.isEmpty() || enfermedadPaciente.isEmpty() ||
                fechaIngresoPaciente.isEmpty()
            ) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                        // Actualizar en la tabla Paciente
                        val updatePaciente = objConexion?.prepareStatement(
                            "UPDATE Paciente SET nombre = ?, apellido = ?, edad = ?, enfermedad = ?, fecha_de_ingreso = ? WHERE id_paciente = ?"
                        )!!
                        updatePaciente.setString(1, nombrePaciente)
                        updatePaciente.setString(2, apellidoPaciente)
                        updatePaciente.setString(3, edadPaciente)
                        updatePaciente.setString(4, enfermedadPaciente)
                        updatePaciente.setString(5, fechaIngresoPaciente)
                        updatePaciente.setInt(6, idPaciente)
                        updatePaciente.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@editar_paciente,
                                "Paciente actualizado exitosamente.",
                                Toast.LENGTH_LONG
                            ).show()
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@editar_paciente,
                                "Ocurrió un error al actualizar la información del paciente. Por favor, intente nuevamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("editar_paciente", "Error: ${e.message}")
                        }
                    }
                }
            }
        }
    }
}