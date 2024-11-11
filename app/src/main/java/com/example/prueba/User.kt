package com.example.prueba

import java.io.Serializable

data class User(

    val firstName: String = "", // Nombre del usuario
    val lastName: String = "",  // Apellido del usuario
    val idNumber: String = "",   // Número de identificación
    val email: String = "",       // Correo electrónico
    var id: String = ""

) : Serializable