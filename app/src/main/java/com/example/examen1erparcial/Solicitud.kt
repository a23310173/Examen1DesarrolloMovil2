package com.example.examen1erparcial

data class Solicitud(
    var curp: String = "",
    var nombre: String = "",
    var apellidos: String = "",
    var domicilio: String = "",
    var ingreso: Double = 0.0,
    var tipoBeca: String = "",
    var apto: Boolean = false
) {
    fun validarIngreso(): Boolean {
        apto = when (tipoBeca.lowercase()) {
            "institucional" -> ingreso in 4000.0..6000.0
            "estatal" -> ingreso > 6000.0 && ingreso <= 8000.0
            "federal" -> ingreso > 8000.0 && ingreso <= 12000.0
            else -> false
        }
        return apto
    }
}