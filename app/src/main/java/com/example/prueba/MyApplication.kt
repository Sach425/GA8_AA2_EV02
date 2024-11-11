package com.example.prueba

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Inicializar Firebase y habilitar persistencia offline
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
