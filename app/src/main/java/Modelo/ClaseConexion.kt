package Modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {

    fun cadenaConexion(): Connection? {
        return try {
            val ip = "jdbc:oracle:thin:@10.10.0.119:1521:xe"
            val usuario = "GABRIEL_DEVELOPER"
            val contrasena = "hKAKI88v"

            // Attempt to establish a connection
            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            conexion
        } catch (e: Exception) {
            // Print the error message if an exception occurs
            println("El error es este: $e")
            null
        }
    }
}