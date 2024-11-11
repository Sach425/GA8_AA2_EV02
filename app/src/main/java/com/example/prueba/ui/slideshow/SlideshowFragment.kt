package com.example.prueba.ui.slideshow


import com.example.prueba.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prueba.User
import com.example.prueba.databinding.FragmentSlideshowBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.util.Log
import androidx.navigation.fragment.findNavController
import com.example.prueba.UserAdapter
import com.example.prueba.ui.edituser.EditUserFragment

class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter
    private val userList: MutableList<User> = mutableListOf()
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inicialización del RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Inicialización de Firebase
        database = FirebaseDatabase.getInstance().getReference("usuarios") // Referencia a la clave "usuarios"

        // Configurar el adapter
        userAdapter = UserAdapter(requireContext(), userList, { user ->
            deleteUser(user)  // Callback para eliminar usuario
        }, { user ->
            editUser(user)    // Callback para editar usuario
        })
        recyclerView.adapter = userAdapter

        // Cargar los usuarios
        loadUsers()

        return root
    }

    private fun loadUsers() {
        // Leer los usuarios desde la clave "usuarios"
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear() // Limpiar la lista antes de agregar los nuevos datos
                for (userSnapshot in snapshot.children) {
                    // Convertir el snapshot en un objeto de tipo User
                    val user = userSnapshot.getValue(User::class.java)
                    // Verificar que el objeto no es nulo antes de agregarlo a la lista
                    if (user != null) {
                        // Asigna el ID (clave generada automáticamente por Firebase)
                        user.id = userSnapshot.key ?: ""  // Asegura que el ID no sea nulo
                        userList.add(user)  // Agregar el usuario a la lista
                    }
                }
                userAdapter.notifyDataSetChanged()  // Notificar al adapter que los datos han cambiado
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SlideshowFragment", "Error al cargar los usuarios: ${error.message}")
            }
        })
    }

    private fun deleteUser(user: User) {
        // Eliminar el usuario de Firebase utilizando su id (clave generada automáticamente)
        database.child(user.id).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userList.remove(user)  // Eliminar el usuario de la lista local
                userAdapter.notifyDataSetChanged()  // Notificar al adapter que los datos han cambiado
            } else {
                Log.e("SlideshowFragment", "Error al eliminar el usuario: ${task.exception?.message}")
            }
        }
    }

    private fun editUser(user: User) {
        // Crear un bundle para pasar el objeto User al fragmento de edición
        val bundle = Bundle()
        bundle.putSerializable("user", user)  // Pasamos el usuario como un objeto serializado

        // Obtén el NavController desde el fragmento
        val navController = findNavController()

        // Navega al fragmento de edición con el bundle
        navController.navigate(R.id.action_nav_slideshow_to_editUser, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Clase de datos con el campo id agregado
data class User(
    val firstName: String = "",
    val lastName: String = "",
    val idNumber: String = "",
    val email: String = "",
    var id: String = ""  // El campo 'id' será la clave generada automáticamente por Firebase
)
