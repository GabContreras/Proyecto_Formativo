package Modelo

data class Paciente (
  val id_paciente: Int,
  val nombre: String,
  val apellido: String,
  val edad: String,
  val enfermedad: String,
  val fecha_de_ingreso: String
)