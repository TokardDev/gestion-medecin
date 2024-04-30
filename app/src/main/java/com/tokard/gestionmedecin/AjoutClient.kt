package com.tokard.gestionmedecin


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class AjoutClient : AppCompatActivity() {
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ajout_client)
        supportActionBar?.title = "Ajout d'un client" // met le titre de l'activité dans la barre d'action

        val maBD = BD(this)

        val typesProfessionnels = listOf("Généraliste", "Pharmacien", "Kiné", "Spécialiste")
        val spinnerType = findViewById<AutoCompleteTextView>(R.id.type)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typesProfessionnels)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerType.setAdapter(adapter)

        var btnAjouterClient = findViewById<Button>(R.id.btnPrendreRdv)
        btnAjouterClient.setOnClickListener {
            // récupère les zones de saisie de texte
            val nom = findViewById<EditText>(R.id.editTextNom).text.toString().uppercase()
            // met la première lettre du prénom en majuscule
            val prenom = findViewById<EditText>(R.id.editTextPrenom).text.toString().lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    java.util.Locale.getDefault()
                ) else it.toString()
            }

            val type = findViewById<AutoCompleteTextView>(R.id.type).text.toString()
            val adresse = findViewById<EditText>(R.id.editTextAdresse).text.toString()
            val codePostal = findViewById<EditText>(R.id.editTextCodePostal).text.toString()
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val telephone = findViewById<EditText>(R.id.editTextTelephone).text.toString()

            // Vérification des champs vides
            if (nom.isEmpty() || prenom.isEmpty() || type.isEmpty() || adresse.isEmpty() ||
                codePostal.isEmpty() || email.isEmpty() || telephone.isEmpty()
            ) {
                // Affiche un Toast indiquant à l'utilisateur de remplir tous les champs
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            } else {
                // Transmet les données à la méthode enregistrerPro de la base de données
                maBD.enregistrerPro(nom, prenom, type, adresse, email, telephone, codePostal)
                // Affiche un Toast indiquant que l'ajout a été effectué avec succès
                Toast.makeText(this, "Client ajouté avec succès", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}