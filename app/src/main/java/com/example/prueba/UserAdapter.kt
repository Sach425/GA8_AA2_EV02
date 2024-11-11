package com.example.prueba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val context: Context,
    private val users: MutableList<User>,
    private val onDeleteClick: (User) -> Unit,
    private val onEditClick: (User) -> Unit // Callback para el clic en editar
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textUser: TextView = view.findViewById(R.id.textUser)
        val textUserTitle: TextView = view.findViewById(R.id.textUserTitle)
        val textEmailTitle: TextView = view.findViewById(R.id.textEmailTitle)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
        val buttonEdit: Button = view.findViewById(R.id.buttonEdit) // Botón de editar
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.textUser.text = "${user.firstName} ${user.lastName}"
        holder.textUserTitle.text = "Usuario: ${user.firstName} ${user.lastName}"
        holder.textEmailTitle.text = "Email: ${user.email}"

        // Configurar el botón de eliminar
        holder.buttonDelete.setOnClickListener {
            showDeleteConfirmationDialog(user)
        }

        // Configurar el botón de editar para llamar al callback
        holder.buttonEdit.setOnClickListener {
            onEditClick(user)
        }
    }

    override fun getItemCount() = users.size

    // Mostrar un diálogo de confirmación antes de eliminar un usuario
    private fun showDeleteConfirmationDialog(user: User) {
        AlertDialog.Builder(context)
            .setTitle("Confirmar Eliminación")
            .setMessage("¿Estás seguro de que deseas eliminar a ${user.firstName} ${user.lastName}?")
            .setPositiveButton("Sí") { _, _ -> onDeleteClick(user) }
            .setNegativeButton("No", null)
            .show()
    }
}
