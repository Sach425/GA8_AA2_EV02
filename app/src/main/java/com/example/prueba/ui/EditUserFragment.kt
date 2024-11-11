package com.example.prueba.ui.edituser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.prueba.R
import com.example.prueba.User
import com.example.prueba.databinding.FragmentEditUserBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditUserFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var successMessage: TextView
    private lateinit var errorMessage: TextView
    private lateinit var saveButton: Button

    private var _binding: FragmentEditUserBinding? = null
    private val binding get() = _binding!!

    private var user: User? = null // Se inicializa como null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditUserBinding.inflate(inflater, container, false)
        val view = binding.root

        // Inicializar Firebase Database
        database = FirebaseDatabase.getInstance().getReference("usuarios")

        // Obtener las vistas desde el binding
        firstNameEditText = binding.firstNameEditText
        lastNameEditText = binding.lastNameEditText
        idNumberEditText = binding.idNumberEditText
        emailEditText = binding.emailEditText
        successMessage = binding.successMessage
        errorMessage = binding.errorMessage
        saveButton = binding.saveButton

        // Recibir el objeto User pasado desde el Fragment anterior
        user = arguments?.getSerializable("user") as? User

        // Si el usuario es válido, cargar sus datos
        user?.let {
            loadUserData(it)
        }

        // Configurar el botón de guardar
        saveButton.setOnClickListener {
            user?.let {
                // Al hacer clic en guardar, actualizamos el usuario
                updateUser(it)
            }
        }

        return view
    }

    private fun loadUserData(user: User) {
        // Rellenar los EditText con los datos del usuario
        firstNameEditText.setText(user.firstName)
        lastNameEditText.setText(user.lastName)
        idNumberEditText.setText(user.idNumber)
        emailEditText.setText(user.email)
    }

    private fun updateUser(user: User) {
        // Obtener los datos modificados desde los EditText
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val idNumber = idNumberEditText.text.toString()
        val email = emailEditText.text.toString()

        // Validar que los campos no estén vacíos
        if (firstName.isEmpty() || lastName.isEmpty() || idNumber.isEmpty() || email.isEmpty()) {
            errorMessage.text = "Por favor, completa todos los campos."
            errorMessage.visibility = View.VISIBLE
            successMessage.visibility = View.GONE
            return
        }

        // Crear el objeto de usuario con los nuevos datos
        val updatedUser = User(firstName, lastName, idNumber, email)

        // Actualizar los datos del usuario en Firebase
        database.child(user.id.toString()).setValue(updatedUser).addOnSuccessListener {
            // Mostrar mensaje de éxito
            successMessage.text = "Usuario actualizado correctamente."
            successMessage.visibility = View.VISIBLE
            errorMessage.visibility = View.GONE

            // Limpiar los campos de texto
            clearFields()
        }.addOnFailureListener {
            // Mostrar mensaje de error
            errorMessage.text = "Error al actualizar el usuario."
            errorMessage.visibility = View.VISIBLE
            successMessage.visibility = View.GONE
        }
    }

    private fun clearFields() {
        // Limpiar los EditText
        firstNameEditText.text.clear()
        lastNameEditText.text.clear()
        idNumberEditText.text.clear()
        emailEditText.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Clase de datos para el Usuario
data class User(
    val id: Int = 0,  // Suponiendo que el usuario tiene un ID único
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val email: String = ""
)
