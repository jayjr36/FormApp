package com.example.formapp.ui.theme

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONException

class DbHelper(context: Context): SQLiteOpenHelper(context, dbName, null,dbVersion) {
    companion object{
        const val dbName = "userData.db"
        const val dbVersion = 1
        const val tableName = "user"
        const val colId = "id"
        const val colName = "name"
        const val colPhone = "phone"
        const val colAge = "age"
        const val colBio = "bio"
        const val colGender = "gender"
        const val colSkill = "skills"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE $tableName (
            $colId INTEGER PRIMARY KEY AUTOINCREMENT,
            $colName TEXT,
            $colPhone TEXT,
            $colAge INTEGER,
            $colBio TEXT,
            $colGender TEXT,
            $colSkill TEXT
            ) 
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
    fun insertUserData(userData: UserData): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(colName, userData.name)
            put(colPhone, userData.phone)
            put(colAge, userData.age)
            put(colBio, userData.bio)
            put(colGender, userData.gender)
            put(colSkill, userData.skills)
        }

        val insertedRowId = db.insert(tableName, null, values)
        db.close()

        return insertedRowId
    }

    @SuppressLint("Range")
    fun getUserData(): List<UserData> {
        val userDataList = mutableListOf<UserData>()
        val db = readableDatabase
        val cursor = db.query(
            tableName, arrayOf(
                colId,
                colName,
                colAge,
                colPhone,
                colBio,
                colGender,
                colSkill
            ),
            null,
            null,
            null,
            null,
            null
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(colId))
            val name = cursor.getString(cursor.getColumnIndex(colName))
            val phone = cursor.getString(cursor.getColumnIndex(colPhone))
            val age = cursor.getInt(cursor.getColumnIndex(colAge))
            val bio = cursor.getString(cursor.getColumnIndex(colBio))
            val gender = cursor.getString(cursor.getColumnIndex(colGender))
            val skillsString = cursor.getString(cursor.getColumnIndex(colSkill))

            val userData = UserData(name, phone, age, bio, gender, skillsString)
            userDataList.add(userData)
        }

        cursor.close()
        db.close()

        return userDataList
    }



}