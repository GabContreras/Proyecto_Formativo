package RecyclerViewHelper

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import gabriel.contreras.proyectoformativo.R

class ViewHolderPaciente(view: View) : RecyclerView.ViewHolder(view) {
    val txtNombrePaciente: TextView = view.findViewById(R.id.txtNombrePaciente)
    val btnBorrar = view.findViewById<ImageView>(R.id.imgBorrar)
    val btnEditar = view.findViewById<ImageView>(R.id.imgEditar)
}
