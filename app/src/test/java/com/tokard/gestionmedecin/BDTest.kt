package com.tokard.gestionmedecin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class BDTest {

    private lateinit var context: Context
    private lateinit var database: BD

    @BeforeEach
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        database = BD(context)
        context.deleteDatabase(BD.DATABASE_NAME)
    }

    @Test
    fun enregistrerPro() {
        val id = database.enregistrerPro("Nom", "Prenom", "Type", "Adresse", "Mail", "Tel", "CodePostal")
        assertTrue(id > 0, "L'enregistrement du professionnel a échoué")
    }

    @Test
    fun prendreRdv() {
        // Enregistrez un professionnel pour obtenir son ID
        val idPro = database.enregistrerPro("Nom", "Prenom", "Type", "Adresse", "Mail", "Tel", "CodePostal")

        // Planifiez un rendez-vous avec l'ID du professionnel
        val idRdv = database.prendreRdv("2023-01-01", "10:00", idPro.toInt())
        assertTrue(idRdv > 0, "La planification du rendez-vous a échoué")
    }

    @Test
    fun listerPro() {
        // Enregistrez quelques professionnels
        database.enregistrerPro("Nom1", "Prenom1", "Type1", "Adresse1", "Mail1", "Tel1", "CodePostal1")
        database.enregistrerPro("Nom2", "Prenom2", "Type2", "Adresse2", "Mail2", "Tel2", "CodePostal2")

        // Liste des professionnels
        val mapPro = database.listerPro()
        assertEquals(2, mapPro.size, "Le nombre de professionnels ne correspond pas")
    }

    @Test
    fun listerProFromCodePostal() {
        // Enregistrez quelques professionnels avec un code postal spécifique
        database.enregistrerPro("Nom1", "Prenom1", "Type1", "Adresse1", "Mail1", "Tel1", "CodePostalTest")
        database.enregistrerPro("Nom2", "Prenom2", "Type2", "Adresse2", "Mail2", "Tel2", "CodePostalTest")

        // Liste des professionnels avec le code postal spécifique
        val mapPro = database.listerProFromCodePostal("CodePostalTest")
        assertEquals(2, mapPro.size, "Le nombre de professionnels avec le code postal spécifique ne correspond pas")
    }

    @Test
    fun listerCodesPostal() {
        // Enregistrez quelques professionnels avec des codes postaux différents
        database.enregistrerPro("Nom1", "Prenom1", "Type1", "Adresse1", "Mail1", "Tel1", "CodePostal1")
        database.enregistrerPro("Nom2", "Prenom2", "Type2", "Adresse2", "Mail2", "Tel2", "CodePostal2")

        // Liste des codes postaux distincts
        val listeCodePostal = database.listerCodesPostal()
        assertEquals(2, listeCodePostal.size, "Le nombre de codes postaux distincts ne correspond pas")
    }

    @Test
    fun getInfoPro() {
        // Enregistrez un professionnel pour obtenir son ID
        val idPro = database.enregistrerPro("Nom", "Prenom", "Type", "Adresse", "Mail", "Tel", "CodePostal")

        // Obtenez les informations du professionnel
        val mapPro = database.getInfoPro(idPro.toInt())
        assertNotNull(mapPro["nom"], "Les informations du professionnel ne sont pas disponibles")
    }
}
