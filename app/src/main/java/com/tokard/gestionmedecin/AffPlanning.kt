package com.tokard.gestionmedecin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.CalendarView
import android.widget.DatePicker
import android.widget.ListView

class AffPlanning : AppCompatActivity() {
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_aff_planning)
        supportActionBar?.title = "Affichage du planning"; // met le titre de l'activité dans la barre d'action

        val calendarPick = findViewById<CalendarView>(R.id.calendarPick)
        val listeRDV = findViewById<ListView>(R.id.listeRDV)

        val date = java.util.Calendar.getInstance() // récupère la date actuelle
        val dayOfMonthStart = date.get(java.util.Calendar.DAY_OF_MONTH)
        val monthStart = date.get(java.util.Calendar.MONTH)
        val yearStart = date.get(java.util.Calendar.YEAR)

        getInfoDate("$dayOfMonthStart/${monthStart+1}/$yearStart")

        calendarPick.setOnDateChangeListener { _, year, month, dayOfMonth -> // quand on change la date dans le calendrier
            getInfoDate("$dayOfMonth/${month+1}/$year")
        }

    }

    @SuppressLint("Range")
    fun getInfoDate(date : String) {

        val maBd = BD(this)
        val listeRDV = findViewById<ListView>(R.id.listeRDV)

        var rdv = maBd.selectFromDay(date)

        var liste = ArrayList<String>() // liste des rdv qu'on pourra transmettre à l'adapter
        while (rdv.moveToNext()) {
            val heure = rdv.getString(rdv.getColumnIndex("heure"))
            val nom = rdv.getString(rdv.getColumnIndex("nom"))
            val prenom = rdv.getString(rdv.getColumnIndex("prenom"))
            val codePostal = rdv.getString(rdv.getColumnIndex("codePostal"))

            liste.add("$heure - $nom $prenom ($codePostal)")
        }

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, liste)
        listeRDV.adapter = adapter
    }



}

