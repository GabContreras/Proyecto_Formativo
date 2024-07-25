package RecyclerViewHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Modelo.ClaseConexion
import Modelo.DetallePaciente
import Modelo.Habitacion_Paciente
import Modelo.Paciente
import gabriel.contreras.proyectoformativo.R
import gabriel.contreras.proyectoformativo.informacion_paciente
import android.content.Intent
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import gabriel.contreras.proyectoformativo.editar_paciente
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.ResultSet


class AdaptadorPaciente(var Datos: List<Paciente>) : RecyclerView.Adapter<ViewHolderPaciente>() {
    //TODO: Terminar el botoncito de editar paciente, y el de mostrar datos de paciente
    fun actualizarLista(nuevaLista: List<Paciente>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notificar al adaptador sobre los cambios
    }

    fun ActualizarLista(nuevaLista: List<Paciente>) {
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    /////////////////// TODO: Eliminar datos
    fun eliminarDatos(id_paciente: Int, posicion: Int) {
        //Actualizo la lista de datos y notifico al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO) {
            //1- Creamos un objeto de la clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //2- Crear una variable que contenga un PrepareStatement
            val borrarPaciente =
                objConexion?.prepareStatement("delete from Paciente where id_paciente = ?")!!
            borrarPaciente.setInt(1, id_paciente)
            borrarPaciente.executeUpdate()


            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()
        }
        Datos = listaDatos.toList()
        // Notificar al adaptador sobre los cambios
        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPaciente {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_card_pacientes, parent, false)
        return ViewHolderPaciente(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolderPaciente, position: Int) {
        //Controlar a la card
        val Paciente = Datos[position]
        holder.txtNombrePaciente.text = Paciente.nombre

        //todo: clic al icono de eliminar
        holder.btnBorrar.setOnClickListener {

            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Desea eliminar al paciente?")

            //Botones
            builder.setPositiveButton("Si") { dialog, which ->
                eliminarDatos(Paciente.id_paciente, position)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        }

        //Todo: icono de editar
        holder.btnEditar.setOnClickListener {
            val context = holder.itemView.context

            val pantallaDetalle = Intent(context, editar_paciente::class.java)
            //enviar a la otra pantalla todos mis valores
            pantallaDetalle.putExtra("id_paciente", Paciente.id_paciente)
            pantallaDetalle.putExtra("nombre", Paciente.nombre)
            pantallaDetalle.putExtra("apellido", Paciente.apellido)
            pantallaDetalle.putExtra("edad", Paciente.edad)
            pantallaDetalle.putExtra("enfermedad", Paciente.enfermedad)
            pantallaDetalle.putExtra("fecha_de_ingreso", Paciente.fecha_de_ingreso)
            context.startActivity(pantallaDetalle)


        }

        holder.itemView.setOnClickListener {
            mostrarDialog(holder.itemView.context, Paciente.id_paciente)
        }
    }

    private fun mostrarDialog(context: Context, idPaciente: Int) {
        Log.d("AdaptadorPaciente", "Mostrar dialog para paciente ID: $idPaciente")
        val builder = AlertDialog.Builder(context)
        val dialogLayout =
            LayoutInflater.from(context).inflate(R.layout.activity_informacion_paciente, null)
        builder.setView(dialogLayout)

        val alertDialog = builder.create()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pacienteDetalles = obtenerDetallesPaciente(idPaciente)

                withContext(Dispatchers.Main) {
                    dialogLayout.findViewById<TextView>(R.id.txtNombrePacienteDetalle)?.text =
                        pacienteDetalles.nombre
                    dialogLayout.findViewById<TextView>(R.id.txtApellidoPacienteDetalle)?.text =
                        pacienteDetalles.apellido
                    dialogLayout.findViewById<TextView>(R.id.txtEdadPacienteDetalle)?.text =
                        pacienteDetalles.edad.toString()
                    dialogLayout.findViewById<TextView>(R.id.txtEnfermedadPacienteDetalle)?.text =
                        pacienteDetalles.enfermedad
                    dialogLayout.findViewById<TextView>(R.id.txtNumeroHabitacionDetalle)?.text =
                        pacienteDetalles.numero_habitacion.toString()
                    dialogLayout.findViewById<TextView>(R.id.txtNumeroDeCamaDetalle)?.text =
                        pacienteDetalles.numero_cama.toString()
                    dialogLayout.findViewById<TextView>(R.id.txtFechaIngresoDetalle)?.text =
                        pacienteDetalles.fecha_de_ingreso
                    dialogLayout.findViewById<TextView>(R.id.txtMedicamentoDetalle)?.text =
                        pacienteDetalles.nombre_medicamento
                    dialogLayout.findViewById<TextView>(R.id.txtHoraAplicacionDetalle)?.text =
                        pacienteDetalles.hora_de_aplicacion

                    alertDialog.show()
                }
            } catch (e: Exception) {
                Log.e("AdaptadorPacientes", "Error al mostrar el dialog", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context,
                        "No se pudo mostrar la información del paciente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun obtenerDetallesPaciente(idPaciente: Int): DetallePaciente {
        val objConexion = ClaseConexion().cadenaConexion()
        if (objConexion == null) {
            throw IllegalStateException("La conexión a la base de datos es nula")
        }

        val statement = objConexion.createStatement()
        val query = """
        SELECT 
            p.nombre AS Nombre,
            p.apellido AS Apellido,
            p.edad AS Edad,
            p.enfermedad AS Enfermedad,
            hp.numero_habitacion AS Numero_Habitacion,
            hp.numero_cama AS Numero_Cama,
            p.fecha_de_ingreso AS Ingreso,
            m.nombre AS Medicamento,
            a.hora_de_aplicacion AS Hora_Aplicacion
        FROM 
            Paciente p
        LEFT JOIN 
            Habitación_paciente hp ON p.id_paciente = hp.id_paciente
        LEFT JOIN 
            Aplicacion_Medicamento a ON p.id_paciente = a.id_paciente
        LEFT JOIN 
            Medicamento m ON a.id_medicamento = m.id_medicamento
        WHERE 
            p.id_paciente = $idPaciente
    """.trimIndent()

        val resultSet: ResultSet = statement.executeQuery(query)
        if (!resultSet.next()) {
            throw IllegalStateException("No se encontraron detalles para el paciente")
        }

        val pacienteDetalles = DetallePaciente(
            nombre = resultSet.getString("Nombre"),
            apellido = resultSet.getString("Apellido"),
            edad = resultSet.getString("Edad"),
            enfermedad = resultSet.getString("Enfermedad"),
            numero_habitacion = resultSet.getInt("Numero_Habitacion"),
            numero_cama = resultSet.getInt("Numero_Cama"),
            fecha_de_ingreso = resultSet.getString("Ingreso"),
            nombre_medicamento = resultSet.getString("Medicamento"),
            hora_de_aplicacion = resultSet.getString("Hora_Aplicacion")
        )

        resultSet.close()
        statement.close()
        objConexion.close()

        return pacienteDetalles
    }
}

