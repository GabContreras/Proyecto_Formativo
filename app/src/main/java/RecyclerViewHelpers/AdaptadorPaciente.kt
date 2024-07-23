package RecyclerViewHelper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import Modelo.ClaseConexion
import Modelo.Paciente
import gabriel.contreras.proyectoformativo.R
import gabriel.contreras.proyectoformativo.informacion_paciente
import android.content.Intent
import android.app.AlertDialog
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            //Creamos un Alert Dialog
            val context = holder.itemView.context

            val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            builder.setTitle("Actualizar")
            builder.setMessage("¿Desea actualizar los datos del paciente?")

        }

        //Todo: Clic a la card completa
        //Vamos a ir a otra pantalla donde me mostrará todos los datos
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context

            //Cambiar de pantalla a la pantalla de detalle
            val pantallaDetalle = Intent(context, informacion_paciente::class.java)
//            enviar a la otra pantalla todos mis valores
//            pantallaDetalle.putExtra("NUMERO_DE_TICKET", ticket.numeroTicket)
//            pantallaDetalle.putExtra("TITULO", ticket.titulo)
//            pantallaDetalle.putExtra("DESCRIPCION", ticket.descripcion)
//            pantallaDetalle.putExtra("AUTOR", ticket.autor)
//            pantallaDetalle.putExtra("EMAIL_AUTOR", ticket.email)
//            pantallaDetalle.putExtra("FECHA_DE_CREACION", ticket.fecha)
//            pantallaDetalle.putExtra("ESTADO", ticket.estado)

            context.startActivity(pantallaDetalle)

        }

    }
}
