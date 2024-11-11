package com.example.prueba.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Esta es la evidencia GA8-220501096-AA2-EV02: ir al menú izquierdo para ingresar datos o visualizar listado de usuarios"
    }
    val text: LiveData<String> = _text
}