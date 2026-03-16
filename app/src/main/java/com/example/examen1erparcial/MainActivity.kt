package com.example.examen1erparcial

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etCurp: EditText
    private lateinit var etNombre: EditText
    private lateinit var etApellidos: EditText
    private lateinit var etDomicilio: EditText
    private lateinit var etIngreso: EditText
    private lateinit var spTipoBeca: Spinner
    private lateinit var btnValidar: Button
    private lateinit var btnLimpiar: Button

    private val solicitud = Solicitud()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(this, "No se concedió permiso para notificaciones", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupSpinner()
        createNotificationChannel()
        requestNotificationPermission()

        btnValidar.setOnClickListener { validarSolicitud() }
        btnLimpiar.setOnClickListener { limpiarCampos() }
    }

    private fun initViews() {
        etCurp = findViewById(R.id.etCurp)
        etNombre = findViewById(R.id.etNombre)
        etApellidos = findViewById(R.id.etApellidos)
        etDomicilio = findViewById(R.id.etDomicilio)
        etIngreso = findViewById(R.id.etIngreso)
        spTipoBeca = findViewById(R.id.spTipoBeca)
        btnValidar = findViewById(R.id.btnValidar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
    }

    private fun setupSpinner() {
        val opciones = listOf("Seleccione una beca", "Institucional", "Estatal", "Federal")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoBeca.adapter = adapter
    }

    private fun validarSolicitud() {
        val curp = etCurp.text.toString().trim()
        val nombre = etNombre.text.toString().trim()
        val apellidos = etApellidos.text.toString().trim()
        val domicilio = etDomicilio.text.toString().trim()
        val ingresoTexto = etIngreso.text.toString().trim()
        val tipoBeca = spTipoBeca.selectedItem.toString()

        if (curp.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() ||
            domicilio.isEmpty() || ingresoTexto.isEmpty() || spTipoBeca.selectedItemPosition == 0
        ) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val ingreso = ingresoTexto.toDoubleOrNull()
        if (ingreso == null) {
            Toast.makeText(this, "El ingreso debe ser numérico", Toast.LENGTH_SHORT).show()
            return
        }

        solicitud.curp = curp
        solicitud.nombre = nombre
        solicitud.apellidos = apellidos
        solicitud.domicilio = domicilio
        solicitud.ingreso = ingreso
        solicitud.tipoBeca = tipoBeca

        if (solicitud.validarIngreso()) {
            mostrarNotificacion()
            Toast.makeText(this, "Apto para la beca $tipoBeca", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No apto para la beca seleccionada", Toast.LENGTH_LONG).show()
        }
    }

    private fun limpiarCampos() {
        etCurp.text.clear()
        etNombre.text.clear()
        etApellidos.text.clear()
        etDomicilio.text.clear()
        etIngreso.text.clear()
        spTipoBeca.setSelection(0)
        etCurp.requestFocus()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_beca",
                "Canal de becas",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones para solicitudes de beca"
            }

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun mostrarNotificacion() {
        val intentRegistro = Intent(this, RegistroActivity::class.java)
        val pendingRegistro = PendingIntent.getActivity(
            this,
            100,
            intentRegistro,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val intentInformes = Intent(this, InformesActivity::class.java)
        val pendingInformes = PendingIntent.getActivity(
            this,
            101,
            intentInformes,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(this, "canal_beca")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Solicitud aprobada")
            .setContentText("Selecciona una opción: Registro o Informes")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(android.R.drawable.ic_menu_my_calendar, "Registro", pendingRegistro)
            .addAction(android.R.drawable.ic_menu_info_details, "Informes", pendingInformes)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(this).notify(1, builder.build())
        }
    }
}