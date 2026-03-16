package com.example.examen1erparcial

object SolicitudRepository {
    val listaSolicitudes = mutableListOf<Solicitud>()

    fun agregarSolicitud(solicitud: Solicitud) {
        listaSolicitudes.add(solicitud.copy())
    }

    fun limpiarSolicitudes() {
        listaSolicitudes.clear()
    }
}