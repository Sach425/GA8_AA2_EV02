package com.example.prueba.ui.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.prueba.databinding.FragmentGalleryBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var idNumberEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var successMessage: TextView
    private lateinit var errorMessage: TextView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel =
            ViewModelProvider(this).get(GalleryViewModel::class.java)

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicializa Firebase Database
        database = FirebaseDatabase.getInstance().getReference("usuarios")

        // Vincula los campos de entrada
        firstNameEditText = binding.firstName
        lastNameEditText = binding.lastName
        idNumberEditText = binding.idNumber
        emailEditText = binding.email
        successMessage = binding.successMessage
        errorMessage = binding.errorMessage

        // Configura el botón para registrar
        val registerButton: Button = binding.btnRegister
        registerButton.setOnClickListener {
            registerUser()
        }

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun registerUser() {
        // Obtiene los datos ingresados
        val firstName = firstNameEditText.text.toString()
        val lastName = lastNameEditText.text.toString()
        val idNumber = idNumberEditText.text.toString()
        val email = emailEditText.text.toString()

        // Validación básica
        if (firstName.isEmpty() || lastName.isEmpty() || idNumber.isEmpty() || email.isEmpty()) {
            errorMessage.text = "Por favor completa todos los campos."
            errorMessage.visibility = View.VISIBLE
            successMessage.visibility = View.GONE
            return
        }

        // Crea un objeto usuario
        val userId = database.push().key // Crea una clave única para el usuario
        val user = User(firstName, lastName, idNumber, email)

        // Guarda los datos en la base de datos
        if (userId != null) {
            database.child(userId).setValue(user).addOnSuccessListener {
                // Muestra mensaje de éxito
                successMessage.text = "Usuario registrado exitosamente."
                successMessage.visibility = View.VISIBLE
                errorMessage.visibility = View.GONE

                // Limpia los campos después del registro
                firstNameEditText.text.clear()
                lastNameEditText.text.clear()
                idNumberEditText.text.clear()
                emailEditText.text.clear()
            }.addOnFailureListener {
                // Maneja el error
                errorMessage.text = "Error al registrar el usuario."
                errorMessage.visibility = View.VISIBLE
                successMessage.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Clase de datos
data class User(val firstName: String, val lastName: String, val idNumber: String, val email: String)
