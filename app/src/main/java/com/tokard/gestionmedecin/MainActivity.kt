package com.tokard.gestionmedecin

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // récupère les boutons
        val ajoutClientButton = findViewById<Button>(R.id.ajoutClientButton)
        var prendreRdvButton = findViewById<Button>(R.id.rdvButton)
        var affPlanningButton = findViewById<Button>(R.id.affPlanningButton)
        var rechercheButton = findViewById<Button>(R.id.rechercheButton)

        supportActionBar?.hide() // cache la barre d'action

        ajoutClientButton.setOnClickListener {
            val intent = Intent(this, AjoutClient::class.java)
            startActivity(intent)
        }

        prendreRdvButton.setOnClickListener {
            val intent = Intent(this, PrendreRdv::class.java)
            startActivity(intent)
        }

        affPlanningButton.setOnClickListener {
            val intent = Intent(this, AffPlanning::class.java)
            startActivity(intent)
        }

        rechercheButton.setOnClickListener {
            val intent = Intent(this, RechercheClient::class.java)
            startActivity(intent)
        }
    }
}