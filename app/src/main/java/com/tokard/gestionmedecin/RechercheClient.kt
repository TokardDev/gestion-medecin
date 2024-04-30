package com.tokard.gestionmedecin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.core.content.ContextCompat

class RechercheClient : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche_client)
        supportActionBar?.title = "Recherche d'un client"

        val nomListe = findViewById<AutoCompleteTextView>(R.id.nomListe)
        val CodePostalListe = findViewById<AutoCompleteTextView>(R.id.CodePostalListe)

        nomListe.setOnItemClickListener { _, _, _, _ -> // quand un element est selectionné on met le texte en noir
            // sert pas à grand chose mais c'est plus joli
            nomListe.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        CodePostalListe.setOnItemClickListener { _, _, _, _ ->
            // pareil
            CodePostalListe.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // récupère la liste des codes postaux disponibles avec listerCodesPostal()
        val maBD = BD(this)
        val listeCodePostal = maBD.listerCodesPostal()
        val adapterCodePostal = ArrayAdapter(this, android.R.layout.simple_list_item_1, listeCodePostal)
        CodePostalListe.setAdapter(adapterCodePostal)
        val affListeClients = findViewById<ListView>(R.id.listeClients)

        // quand codePostalListe est changé on utilise listerProFromCodePostal() pour récupérer les professionnels
        // on affiche les pros "nom prenom" dans nomListe
        CodePostalListe.setOnItemClickListener { _, _, _, _ ->
            CodePostalListe.setTextColor(ContextCompat.getColor(this, R.color.black))
            nomListe.setText("Client")
            // on vide affListeClients
            val listeVide = ArrayList<String>()
            nomListe.setTextColor(ContextCompat.getColor(this, R.color.grey))
            val hashPro = maBD.listerProFromCodePostal(CodePostalListe.text.toString()) // le hashmap contient les noms et prénoms et les id "nom prenom" -> id
            // on convertit le hashmap en arraylist pour récupérer uniquement les noms et prénoms
            val listePro = ArrayList<String>()
            for (i in hashPro) {
                listePro.add(i.key)
            }

            val adapterPro = ArrayAdapter(this, android.R.layout.simple_list_item_1, listePro)
            nomListe.setAdapter(adapterPro)
        }

        nomListe.setOnItemClickListener() { _, _, _, _ ->
            nomListe.setTextColor(ContextCompat.getColor(this, R.color.black))
            // récupère les infos du pro dans un hashmap avec getInfoPro(id)
            val idPro = maBD.listerProFromCodePostal(CodePostalListe.text.toString())[nomListe.text.toString()]
            val hashPro = maBD.getInfoPro(idPro!!)
            // on ajoute chaque information sur une nouvelle ligne dans affListeClients
            val listeInfoPro = ArrayList<String>()
            for (i in hashPro) {
                listeInfoPro.add(i.key + " : " + i.value)
            }
            val adapterInfoPro = ArrayAdapter(this, android.R.layout.simple_list_item_1, listeInfoPro)
            affListeClients.adapter = adapterInfoPro

        }

    }
}
