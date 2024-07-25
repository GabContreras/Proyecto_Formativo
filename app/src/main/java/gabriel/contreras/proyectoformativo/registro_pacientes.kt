package gabriel.contreras.proyectoformativo

import Modelo.ClaseConexion
import Modelo.Habitacion
import Modelo.Medicamento
import Modelo.Paciente
import RecyclerViewHelper.AdaptadorPaciente
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Toast
import android.icu.util.Calendar
import android.app.DatePickerDialog
import android.content.Intent
import android.widget.ImageView
import kotlinx.coroutines.GlobalScope
import java.sql.Statement
import java.text.SimpleDateFormat
import java.util.Locale


class registro_pacientes : AppCompatActivity() {
    private var fechaSelecccionada: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_pacientes)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtNombrePaciente = findViewById<EditText>(R.id.txtNombrePacienteIngreso)
        val txtApellidoPaciente = findViewById<EditText>(R.id.txtApellidoPacienteIngreso)
        val txtEdadPaciente = findViewById<EditText>(R.id.txtEdadPacienteIngreso)
        val txtEnfermedadPaciente = findViewById<EditText>(R.id.txtEnfermedadPacienteIngreso)
        val txtFechaIngresoPaciente = findViewById<EditText>(R.id.txtFechaDeIngresoPacienteIngreso)
        val txtNumeroCamaPaciente = findViewById<EditText>(R.id.txtNumeroDeCamaIngreso)
        val spHabitacion = findViewById<Spinner>(R.id.spHabitacionPaciente)
        val spMedicamento = findViewById<Spinner>(R.id.spMedicamento)
        val txthoraAplicacion = findViewById<EditText>(R.id.txtHoraDeAPlicacionIngreso)
        val btnRegistrar = findViewById<ImageButton>(R.id.btnRegistrarPacienteIngreso)
        val btncerrar = findViewById<ImageView>(R.id.btnSalirRegistro)

        btncerrar.setOnClickListener {
            val pantallaPacientes = Intent(this, lista_pacientes::class.java)
            startActivity(pantallaPacientes)
            finish()
        }
        txtFechaIngresoPaciente.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            // Calcular la fecha máxima (fecha actual)
            val fechaMaxima = Calendar.getInstance()
            fechaMaxima.set(anio, mes, dia)

            val datePickerDialog = DatePickerDialog(
                this, { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fechaIngresada = dateFormat.parse(fechaSeleccionada)
                    val fechaActual = Calendar.getInstance().time

                    if (fechaIngresada != null && fechaIngresada.after(fechaActual)) {
                        Toast.makeText(this, "La fecha seleccionada no puede ser una fecha futura", Toast.LENGTH_SHORT).show()
                    } else {
                        txtFechaIngresoPaciente.setText(fechaSeleccionada)
                        fechaSelecccionada = fechaSeleccionada
                    }
                }, anio, mes, dia
            )

            // Configurar la fecha máxima a la fecha actual
            datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

            datePickerDialog.show()
        }
        // Función para hacer el select de las Habitaciones
        fun obtenerHabitaciones(): List<Habitacion> {
            val listadoDeHabitaciones = mutableListOf<Habitacion>()
            val objConexion = ClaseConexion().cadenaConexion()

            if (objConexion != null) {
                // Creo un Statement que me ejecutará el select
                val statement = objConexion.createStatement()
                val resultSet = statement?.executeQuery("select * from Habitacion")

                if (resultSet != null) {
                    while (resultSet.next()) {
                        val numero_habitacion = resultSet.getInt("numero_habitacion")
                        val listadoCompleto = Habitacion(numero_habitacion)
                        listadoDeHabitaciones.add(listadoCompleto)
                    }
                    resultSet.close()
                }
                statement?.close()
                objConexion.close()
            } else {
                Log.e("registro_pacientes", "Connection to database failed")
            }

            return listadoDeHabitaciones
        }
        CoroutineScope(Dispatchers.IO).launch {
            val listadoDeHabitaciones = obtenerHabitaciones()
            val habitaciones = listadoDeHabitaciones.map { it.numero_habitacion }

            withContext(Dispatchers.Main) {
                // Configuración del adaptador
                val adapter = ArrayAdapter(
                    this@registro_pacientes, // Usar el contexto adecuado
                    android.R.layout.simple_spinner_dropdown_item,
                    habitaciones
                )
                spHabitacion.adapter = adapter
            }
        }

        // Función para hacer el select de los Medicamentos
        fun obtenerMedicamentos(): List<Medicamento> {
            val listadoDeMedicamentos = mutableListOf<Medicamento>()
            val objConexion = ClaseConexion().cadenaConexion()

            if (objConexion != null) {
                // Creo un Statement que me ejecutará el select
                val statement = objConexion.createStatement()
                val resultSet = statement?.executeQuery("select * from Medicamento")

                if (resultSet != null) {
                    while (resultSet.next()) {
                        val id_medicamento = resultSet.getInt("id_medicamento")
                        val nombre = resultSet.getString("nombre")
                        val descripcion = resultSet.getString("descripcion")
                        val listadoCompleto = Medicamento(id_medicamento, nombre, descripcion)
                        listadoDeMedicamentos.add(listadoCompleto)
                    }
                    resultSet.close()
                }
                statement?.close()
                objConexion.close()
            } else {
                Log.e("registro_pacientes", "Connection to database failed")
            }

            return listadoDeMedicamentos
        }

        CoroutineScope(Dispatchers.IO).launch {
            val listadoDeMedicamentos = obtenerMedicamentos()
            val medicamentos = listadoDeMedicamentos.map { it.nombre }

            withContext(Dispatchers.Main) {
                // Configuración del adaptador
                val adapter = ArrayAdapter(
                    this@registro_pacientes, // Usar el contexto adecuado
                    android.R.layout.simple_spinner_dropdown_item,
                    medicamentos
                )
                spMedicamento.adapter = adapter
            }
        }

        btnRegistrar.setOnClickListener {
            val nombrePaciente = txtNombrePaciente.text.toString()
            val apellidoPaciente = txtApellidoPaciente.text.toString()
            val edadPaciente = txtEdadPaciente.text.toString()
            val enfermedadPaciente = txtEnfermedadPaciente.text.toString()
            val fechaIngresoPaciente = fechaSelecccionada.toString()
            val numeroCamaPaciente = txtNumeroCamaPaciente.text.toString().toIntOrNull()
            val numeroHabitacionPaciente = spHabitacion.selectedItem.toString().toIntOrNull()
            val nombreMedicamento = spMedicamento.selectedItem.toString()
            val horaAplicacion = txthoraAplicacion.text.toString()

            // Validar la hora de aplicación
            val horaPattern = "^([01][0-9]|2[0-3]):[0-5][0-9](AM|PM)$".toRegex()


            if (!horaPattern.matches(horaAplicacion)) {
                Toast.makeText(this, "La hora debe estar en formato hh:mmAM/PM, ejemplo: 10:00AM", Toast.LENGTH_SHORT).show()
            }
            else if (nombrePaciente.isEmpty() || apellidoPaciente.isEmpty() || edadPaciente.isEmpty() || enfermedadPaciente.isEmpty() ||
                fechaIngresoPaciente.isEmpty() || numeroCamaPaciente == null || numeroHabitacionPaciente == null
            ) {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val objConexion = ClaseConexion().cadenaConexion()

                        // Insertar en la tabla Paciente
                        val insertPaciente = objConexion?.prepareStatement(
                            "INSERT INTO Paciente(nombre, apellido, edad, enfermedad, fecha_de_ingreso) VALUES (?, ?, ?, ?, ?)"
                        )
                        insertPaciente?.setString(1, nombrePaciente)
                        insertPaciente?.setString(2, apellidoPaciente)
                        insertPaciente?.setString(3, edadPaciente)
                        insertPaciente?.setString(4, enfermedadPaciente)
                        insertPaciente?.setString(5, fechaIngresoPaciente)
                        insertPaciente?.executeUpdate()

                        // Obtener el ID del paciente usando un SELECT
                        val selectPacienteId = objConexion?.prepareStatement(
                            "SELECT id_paciente FROM Paciente WHERE nombre = ? AND apellido = ? AND edad = ? AND enfermedad = ? AND fecha_de_ingreso = ?"
                        )
                        selectPacienteId?.setString(1, nombrePaciente)
                        selectPacienteId?.setString(2, apellidoPaciente)
                        selectPacienteId?.setString(3, edadPaciente)
                        selectPacienteId?.setString(4, enfermedadPaciente)
                        selectPacienteId?.setString(5, fechaIngresoPaciente)
                        val resultSet = selectPacienteId?.executeQuery()

                        var idPaciente: Int? = null
                        if (resultSet != null && resultSet.next()) {
                            idPaciente = resultSet.getInt("id_paciente")
                        }

                        // Insertar en la tabla Habitación_paciente
                        val insertHabitacionPaciente = objConexion?.prepareStatement(
                            "INSERT INTO Habitación_paciente(numero_cama, numero_habitacion, id_paciente) VALUES (?, ?, ?)"
                        )
                        insertHabitacionPaciente?.setInt(1, numeroCamaPaciente ?: 0)
                        insertHabitacionPaciente?.setInt(2, numeroHabitacionPaciente ?: 0)
                        insertHabitacionPaciente?.setInt(3, idPaciente ?: 0)
                        insertHabitacionPaciente?.executeUpdate()

                        // Obtener el id_medicamento desde el Spinner
                        val medicamentos = obtenerMedicamentos() // Se asume que puedes obtener la lista de medicamentos aquí
                        val medicamentoSeleccionado = medicamentos.find { it.nombre == nombreMedicamento }
                        val idMedicamento = medicamentoSeleccionado?.id_medicamento

                        // Insertar en la tabla Aplicacion_Medicamento
                        val insertAplicacionMedicamento = objConexion?.prepareStatement(
                            "INSERT INTO Aplicacion_Medicamento(id_medicamento, id_paciente, hora_de_aplicacion) VALUES (?, ?, ?)"
                        )
                        insertAplicacionMedicamento?.setInt(1, idMedicamento ?: 0)
                        insertAplicacionMedicamento?.setInt(2, idPaciente ?: 0)
                        insertAplicacionMedicamento?.setString(3, horaAplicacion)
                        insertAplicacionMedicamento?.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@registro_pacientes,
                                "Paciente registrado exitosamente.",
                                Toast.LENGTH_LONG
                            ).show()
                            txtNombrePaciente.setText("")
                            txtApellidoPaciente.setText("")
                            txtEdadPaciente.setText("")
                            txtEnfermedadPaciente.setText("")
                            txtFechaIngresoPaciente.setText("")
                            txtNumeroCamaPaciente.setText("")
                            txthoraAplicacion.setText("")
                        }

                        resultSet?.close()
                        selectPacienteId?.close()
                        insertPaciente?.close()
                        objConexion?.close()

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@registro_pacientes,
                                "Ocurrió un error al registrar el paciente. Por favor, intente nuevamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("registro_pacientes", "Error: ${e.message}")
                        }
                    }
                }
            }
        }

    }
}