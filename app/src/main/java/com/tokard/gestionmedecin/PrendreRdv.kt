package com.tokard.gestionmedecin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat

class PrendreRdv : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prendre_rdv)
        supportActionBar?.title = "Prise de rendez-vous"

        // quand dateButton est cliqué, affiche le calendarPick avec une animation
        val dateButton = findViewById<Button>(R.id.dateButton)
        val calendarPick = findViewById<CalendarView>(R.id.calendarPick)
        val timePickerRdv = findViewById<TimePicker>(R.id.timePickerRdv)
        val heureButton = findViewById<Button>(R.id.heureButton)

        timePickerRdv.setIs24HourView(true) // met le timePicker en 24h
        calendarPick.visibility = CalendarView.GONE
        timePickerRdv.visibility = TimePicker.GONE


        dateButton.setOnClickListener { // quand on clique sur dateButton
            if (calendarPick.visibility == CalendarView.GONE) { // gère la visibilité des éléments pour ne pas surcharger l'interface
                calendarPick.visibility = CalendarView.VISIBLE
                timePickerRdv.visibility = TimePicker.GONE
            } else {
                calendarPick.visibility = CalendarView.GONE
            }
        }

        heureButton.setOnClickListener {
            if (timePickerRdv.visibility == TimePicker.GONE) {
                timePickerRdv.visibility = TimePicker.VISIBLE
                calendarPick.visibility = CalendarView.GONE
            } else {
                timePickerRdv.visibility = TimePicker.GONE
            }
        }

        calendarPick.setOnDateChangeListener { _, year, month, dayOfMonth -> // quand on change la date dans le calendrier
            var dateSelectionnee = "$dayOfMonth/${month+1}/$year"
            dateButton.text = dateSelectionnee
        }

        timePickerRdv.setOnTimeChangedListener { _, hourOfDay, minute ->
            var heureSelectionnee = "$hourOfDay:$minute"
            heureButton.text = heureSelectionnee
        }




        // récupère tout les medecins de la base de données et les affiche dans le spinner spinnerProConcerne
        val maBD = BD(this)
        val listePro = maBD.listerPro() // hashmap
        val spinnerProConcerne = findViewById<AutoCompleteTextView>(R.id.proConcerne)

        // quand on change de professionnel, on met la couleur du texte en noir
        spinnerProConcerne.setOnItemClickListener { _, _, _, _ ->
            spinnerProConcerne.setTextColor(ContextCompat.getColor(this, R.color.black))
        }

        // fais une liste avec les noms des professionnels
        // liste pro est tel que "nom prenom" : clé (int)
        val listeNomPro = listePro.keys.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listeNomPro)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProConcerne.setAdapter(adapter)


        var btnPrendreRdv = findViewById<Button>(R.id.btnPrendreRdv)

        btnPrendreRdv.setOnClickListener {

            // récupère l'id du professionnel sélectionné
            val nomPro = spinnerProConcerne.text.toString()
            val idPro = listePro[nomPro]

            // récupère la date et l'heure sélectionnée à partir
            val dateRdv = dateButton.text.toString()
            val heureRdv = heureButton.text.toString()

            // insère les données dans la bdd avec la méthode enregistrerRdv
            if (idPro != null && dateRdv != "date" && heureRdv != "heure") {
                maBD.prendreRdv(dateRdv, heureRdv, idPro)
                Toast.makeText(this, "Rendez-vous pris avec succès", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                if (dateRdv == "date")
                    Toast.makeText(this, "Merci de selectionner une date", Toast.LENGTH_SHORT).show()
                else if (heureRdv == "heure")
                    Toast.makeText(this, "Merci de selectionner une heure", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Merci de selectionner un client", Toast.LENGTH_SHORT).show()
            }
        }



    }
}