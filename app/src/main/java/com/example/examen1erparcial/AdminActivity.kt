package com.example.examen1erparcial

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var tvSolicitudes: TextView
    private lateinit var btnActualizar: Button
    private lateinit var btnLimpiarLista: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        tvSolicitudes = findViewById(R.id.tvSolicitudes)
        btnActualizar = findViewById(R.id.btnActualizar)
        btnLimpiarLista = findViewById(R.id.btnLimpiarLista)

        mostrarSolicitudes()

        btnActualizar.setOnClickListener {
            mostrarSolicitudes()
        }

        btnLimpiarLista.setOnClickListener {
            SolicitudRepository.limpiarSolicitudes()
            mostrarSolicitudes()
        }
    }

    private fun mostrarSolicitudes() {
        if (SolicitudRepository.listaSolicitudes.isEmpty()) {
            tvSolicitudes.text = "No hay solicitudes registradas."
            return
        }

        val texto = StringBuilder()

        SolicitudRepository.listaSolicitudes.forEachIndexed { index, solicitud ->
            texto.append("Solicitud ${index + 1}\n")
            texto.append("CURP: ${solicitud.curp}\n")
            texto.append("Nombre: ${solicitud.nombre} ${solicitud.apellidos}\n")
            texto.append("Domicilio: ${solicitud.domicilio}\n")
            texto.append("Ingreso: ${solicitud.ingreso}\n")
            texto.append("Beca: ${solicitud.tipoBeca}\n")
            texto.append("Resultado: ${if (solicitud.apto) "Apto" else "No apto"}\n")
            texto.append("\n-----------------------------\n\n")
        }

        tvSolicitudes.text = texto.toString()
    }
}