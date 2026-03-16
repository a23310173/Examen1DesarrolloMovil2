package com.example.examen1erparcial

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AccesoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acceso)

        val btnUsuario = findViewById<Button>(R.id.btnUsuario)
        val btnAdmin = findViewById<Button>(R.id.btnAdmin)

        btnUsuario.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        btnAdmin.setOnClickListener {
            startActivity(Intent(this, AdminActivity::class.java))
        }
    }
}