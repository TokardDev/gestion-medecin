package com.tokard.gestionmedecin

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BD(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "GestionMedecin.db"
        private const val DATABASE_VERSION = 1

        // Table Professionnel
        private const val TABLE_PROFESSIONNEL = "professionnel"
        private const val COL_ID = "id"
        private const val COL_NOM = "nom"
        private const val COL_PRENOM = "prenom"
        private const val COL_TYPE = "type"
        private const val COL_ADRESSE = "adresse"
        private const val COL_MAIL = "mail"
        private const val COL_TEL = "tel"
        private const val COL_CODEPOSTAL = "codepostal"

        // Table RDV
        private const val TABLE_RDV = "rdv"
        private const val COL_DATE = "date"
        private const val COL_HEURE = "heure"
        private const val COL_ID_PRO = "idPro"
    }

    /**
     * Création des tables
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createProfessionnelTable = """
            CREATE TABLE $TABLE_PROFESSIONNEL (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOM TEXT,
                $COL_PRENOM TEXT,
                $COL_TYPE TEXT,
                $COL_ADRESSE TEXT,
                $COL_MAIL TEXT,
                $COL_TEL TEXT,
                $COL_CODEPOSTAL TEXT
            )
        """.trimIndent()

        val createRdvTable = """
            CREATE TABLE $TABLE_RDV (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DATE TEXT,
                $COL_HEURE TEXT,
                $COL_ID_PRO INTEGER,
                FOREIGN KEY($COL_ID_PRO) REFERENCES $TABLE_PROFESSIONNEL($COL_ID)
            )
        """.trimIndent()

        db.execSQL(createProfessionnelTable)
        db.execSQL(createRdvTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    /**
     * ajout d'un professionnel
     * @param nom
     * @param prenom
     * @param type
     * @param adresse
     * @param mail
     * @param tel
     * @param codePostal
     */
    fun enregistrerPro(
        nom: String,
        prenom: String,
        type: String,
        adresse: String,
        mail: String,
        tel: String,
        codePostal: String
    ): Long {
        val values = ContentValues().apply {
            put(COL_NOM, nom)
            put(COL_PRENOM, prenom)
            put(COL_TYPE, type)
            put(COL_ADRESSE, adresse)
            put(COL_MAIL, mail)
            put(COL_TEL, tel)
            put(COL_CODEPOSTAL, codePostal)
        }


        return writableDatabase.insert(TABLE_PROFESSIONNEL, null, values)
    }

    /**
     * ajout d'un rendez-vous
     * @param date
     * @param heure
     * @param idPro
     */
    fun prendreRdv(date: String, heure: String, idPro: Int): Long {
        val values = ContentValues().apply {
            put(COL_DATE, date)
            put(COL_HEURE, heure)
            put(COL_ID_PRO, idPro)
        }

        return writableDatabase.insert(TABLE_RDV, null, values)
    }


    /**
     * selection des rendez-vous d'un jour precis
     * @param selectedDate jj/mm/aaaa
     */
    fun selectFromDay(selectedDate: String): Cursor {
        val query = """
            SELECT $TABLE_RDV.$COL_DATE as date, $TABLE_RDV.$COL_HEURE as heure, 
                   $TABLE_PROFESSIONNEL.$COL_NOM as nom, $TABLE_PROFESSIONNEL.$COL_PRENOM as prenom,
                   $TABLE_PROFESSIONNEL.$COL_CODEPOSTAL as codePostal
            FROM $TABLE_RDV
            INNER JOIN $TABLE_PROFESSIONNEL ON $TABLE_RDV.$COL_ID_PRO = $TABLE_PROFESSIONNEL.$COL_ID
            WHERE $TABLE_RDV.$COL_DATE = ?
            ORDER BY $TABLE_RDV.$COL_HEURE ASC;
        """.trimIndent()
        val selectionArgs = arrayOf(selectedDate)
        return readableDatabase.rawQuery(query, selectionArgs)
    }

    /**
     * liste des professionnels
     * @return HashMap<String, Int> "nom prenom" -> id
     */
    @SuppressLint("Range")
    fun listerPro(): HashMap<String, Int> {
        val mapPro = hashMapOf<String, Int>()
        val query = "SELECT * FROM $TABLE_PROFESSIONNEL"
        val cursor = readableDatabase.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val nom = cursor.getString(cursor.getColumnIndex(COL_NOM))
            val prenom = cursor.getString(cursor.getColumnIndex(COL_PRENOM))
            val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            mapPro["$nom $prenom"] = id
        }
        cursor.close()
        return mapPro
    }

    /**
     * liste des professionnels d'un code postal
     * @param codePostal
     * @return HashMap<String, Int> "nom prenom" -> id
     */
    @SuppressLint("Range")
    fun listerProFromCodePostal(codePostal: String): HashMap<String, Int> {
        val mapPro = hashMapOf<String, Int>()
        val query = "SELECT * FROM $TABLE_PROFESSIONNEL WHERE $COL_CODEPOSTAL = ?"
        val selectionArgs = arrayOf(codePostal)
        val cursor = readableDatabase.rawQuery(query, selectionArgs)
        while (cursor.moveToNext()) {
            val nom = cursor.getString(cursor.getColumnIndex(COL_NOM))
            val prenom = cursor.getString(cursor.getColumnIndex(COL_PRENOM))
            val id = cursor.getInt(cursor.getColumnIndex(COL_ID))
            mapPro["$nom $prenom"] = id
        }
        cursor.close()
        return mapPro
    }


    /**
     * liste des codes postaux distincts
     * @return ArrayList<String> liste des codes postaux
     */
    @SuppressLint("Range")
    fun listerCodesPostal(): ArrayList<String> {
        val listeCodePostal = ArrayList<String>()
        val query = "SELECT DISTINCT $COL_CODEPOSTAL FROM $TABLE_PROFESSIONNEL"
        val cursor = readableDatabase.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val codePostal = cursor.getString(cursor.getColumnIndex(COL_CODEPOSTAL))
            listeCodePostal.add(codePostal)
        }
        cursor.close()
        return listeCodePostal
    }

    /**
     * informations d'un professionnel
     * @param idPro
     * @return HashMap<String, String> "nom" -> nom, "prenom" -> prenom, "type" -> type, "adresse" -> adresse, "mail" -> mail, "tel" -> tel, "codePostal" -> codePostal
     */
    @SuppressLint("Range")
    fun getInfoPro(idPro: Int): HashMap<String, String> {
        val mapPro = hashMapOf<String, String>()
        val query = "SELECT * FROM $TABLE_PROFESSIONNEL WHERE $COL_ID = ?"
        val selectionArgs = arrayOf(idPro.toString())
        val cursor = readableDatabase.rawQuery(query, selectionArgs)
        while (cursor.moveToNext()) {
            val nom = cursor.getString(cursor.getColumnIndex(COL_NOM))
            val prenom = cursor.getString(cursor.getColumnIndex(COL_PRENOM))
            val type = cursor.getString(cursor.getColumnIndex(COL_TYPE))
            val adresse = cursor.getString(cursor.getColumnIndex(COL_ADRESSE))
            val mail = cursor.getString(cursor.getColumnIndex(COL_MAIL))
            val tel = cursor.getString(cursor.getColumnIndex(COL_TEL))
            val codePostal = cursor.getString(cursor.getColumnIndex(COL_CODEPOSTAL))
            mapPro["nom"] = nom
            mapPro["prenom"] = prenom
            mapPro["type"] = type
            mapPro["adresse"] = adresse
            mapPro["mail"] = mail
            mapPro["tel"] = tel
            mapPro["codePostal"] = codePostal
        }
        cursor.close()
        return mapPro
    }
}
