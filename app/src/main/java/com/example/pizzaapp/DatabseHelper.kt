package com.example.pizzaapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabseHelper(context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABSE_VERSION){

    companion object {
        private  val DATABASE_NAME = "pizza"
        private  val DATABSE_VERSION = 1

        // TABLES
        private  val TABLE_ACCOUNT = "account"

        // COLUMS
        private  val COLUMN_EMAIL = "email"
        private  val COLUMN_NAME = "name"
        private  val COLUMN_LEVEL = "level"
        private  val COLUMN_PASSWORD = "password"

    }

    private  val CREATE_ACCOUNT_TABLE = ("CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY, " + COLUMN_NAME + " TEXT, "
            + COLUMN_LEVEL + " TEXT , " + COLUMN_PASSWORD + " TEXT)")

    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        onCreate(p0)
    }

    //lOGIN CHECK
    fun  checkLogin(email:String, password:String): Boolean{
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase

        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"

        val selectionArgs = arrayOf(email, password)

        val cursor = db.query(TABLE_ACCOUNT, colums, selection, selectionArgs, null, null, null)

        var cursorCount = cursor.count
        cursor.close()
        db.close()

        return cursorCount > 0
    }


}