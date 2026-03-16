package com.example.examen1erparcial

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {

    private lateinit var tvFecha: TextView
    private lateinit var tvHora: TextView
    private lateinit var btnSeleccionarFecha: Button
    private lateinit var btnSeleccionarHora: Button
    private lateinit var btnAgendar: Button

    private var fechaSeleccionada = "Sin fecha"
    private var horaSeleccionada = "Sin hora"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        tvFecha = findViewById(R.id.tvFechaSeleccionada)
        tvHora = findViewById(R.id.tvHoraSeleccionada)
        btnSeleccionarFecha = findViewById(R.id.btnSeleccionarFecha)
        btnSeleccionarHora = findViewById(R.id.btnSeleccionarHora)
        btnAgendar = findViewById(R.id.btnAgendar)

        btnSeleccionarFecha.setOnClickListener { mostrarDatePicker() }
        btnSeleccionarHora.setOnClickListener { mostrarTimePicker() }

        btnAgendar.setOnClickListener {
            Toast.makeText(
                this,
                "Cita agendada para: $fechaSeleccionada a las $horaSeleccionada",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()
        val year = calendario.get(Calendar.YEAR)
        val month = calendario.get(Calendar.MONTH)
        val day = calendario.get(Calendar.DAY_OF_MONTH)

        val dialog = DatePickerDialog(this, { _, y, m, d ->
            fechaSeleccionada = "$d/${m + 1}/$y"
            tvFecha.text = "Fecha: $fechaSeleccionada"
        }, year, month, day)

        dialog.show()
    }

    private fun mostrarTimePicker() {
        val calendario = Calendar.getInstance()
        val hour = calendario.get(Calendar.HOUR_OF_DAY)
        val minute = calendario.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(this, { _, h, min ->
            horaSeleccionada = String.format("%02d:%02d", h, min)
            tvHora.text = "Hora: $horaSeleccionada"
        }, hour, minute, true)

        dialog.show()
    }
}