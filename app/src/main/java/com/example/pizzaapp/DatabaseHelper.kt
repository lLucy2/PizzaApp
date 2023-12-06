package com.example.pizzaapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import com.example.pizzaapp.model.MenuModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class DatabaseHelper(var context: Context): SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION){

    companion object {

            private val DATABASE_NAME = "pizza"
            private val DATABASE_VERSION = 1

            // TABLE ACCOUNT
            private val TABLE_ACCOUNT = "account"

            // COLUMS
            private val COLUMN_EMAIL = "email"
            private val COLUMN_NAME = "name"
            private val COLUMN_LEVEL = "level"
            private val COLUMN_PASSWORD = "password"


            // TABLE MENU
            private val TABLE_MENU = "menu"

            // COLUMS
            private val COLUMN_ID_MENU = "idMenu"
            private val COLUMN_NAMA_MENU = "menuName"
            private val COLUMN_PRICE_MENU = "price"
            private val COLUMN_IMAGE = "photo"


    }

    private  val CREATE_ACCOUNT_TABLE = ("CREATE TABLE " + TABLE_ACCOUNT + "("
            + COLUMN_EMAIL + " TEXT PRIMARY KEY, " + COLUMN_NAME + " TEXT, "
            + COLUMN_LEVEL + " TEXT , " + COLUMN_PASSWORD + " TEXT)")

    private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"


//    private val CREATE_MENU_TABLE = ("CREATE TABLE " + TABLE_MENU + "(" + COLUMN_ID_MENU + " INT PRIMARY KEY, " + COLUMN_NAMA_MENU + " TEXT, " + COLUMN_PRICE_MENU + " INT, " + COLUMN_IMAGE + " BLOB")
    private val CREATE_MENU_TABLE = ("CREATE TABLE " + TABLE_MENU + "(" + COLUMN_ID_MENU + " INT PRIMARY KEY, " + COLUMN_NAMA_MENU + " TEXT, " + COLUMN_PRICE_MENU + " INT, " + COLUMN_IMAGE + " BLOB)")

    private  val DROP_MENU_TABLE = "DROP TABLE IF EXISTS $TABLE_MENU"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_ACCOUNT_TABLE)
        p0?.execSQL(CREATE_MENU_TABLE)
        //p0?.execSQL(INSERT_ACCOUNT_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL(DROP_ACCOUNT_TABLE)
        p0?.execSQL(DROP_MENU_TABLE)
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

    fun addAcount(email:String, name:String, level:String, password:String){
        val db = this.readableDatabase

        val values = ContentValues()
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_LEVEL, level)
        values.put(COLUMN_PASSWORD, password)

        val result = db.insert(TABLE_ACCOUNT, null, values)
        // show message
        if (result == (0).toLong()){
            Toast.makeText(context, "Register Failed", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(context, "Register Success, Please Login Using Your New Account", Toast.LENGTH_SHORT).show()
        }

        db.close()

    }

    @SuppressLint("Range")
    fun checkData(email:String): String {
        val colums = arrayOf(COLUMN_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_EMAIL = ?"
        val selectionArgs = arrayOf(email)
        var name:String = ""

        val cursor = db.query(TABLE_ACCOUNT, colums, selection, selectionArgs, null, null, null)

        if(cursor.moveToFirst()){
            name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
        }


    cursor.close()
    db.close()
    return name

    }


    fun showMenu(): ArrayList<MenuModel> {
        val listModel = ArrayList<MenuModel>()
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU, null)
        } catch (se: SQLiteException) {
            db.execSQL(CREATE_MENU_TABLE)
            return ArrayList()
        }

        var id: Int
        var name: String
        var price: Int
        var imageArray: ByteArray
        var imageBmp: Bitmap

        if (cursor.moveToFirst()) {
            do {
                // get data text
                val idIndex = cursor.getColumnIndex(COLUMN_ID_MENU)
                val nameIndex = cursor.getColumnIndex(COLUMN_NAMA_MENU)
                val priceIndex = cursor.getColumnIndex(COLUMN_PRICE_MENU)
                val imageIndex = cursor.getColumnIndex(COLUMN_IMAGE)

                // Check if the indices are valid
                if (idIndex >= 0 && nameIndex >= 0 && priceIndex >= 0 && imageIndex >= 0) {
                    id = cursor.getInt(idIndex)
                    name = cursor.getString(nameIndex)
                    price = cursor.getInt(priceIndex)

                    // get data image
                    imageArray = cursor.getBlob(imageIndex)

                    // convert byteArray to bitmap
                    val byteInputStream = ByteArrayInputStream(imageArray)
                    imageBmp = BitmapFactory.decodeStream(byteInputStream)
                    val model = MenuModel(id = id, name = name, price = price, image = imageBmp)

                    listModel.add(model)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return listModel
    }

    fun addMenu(menu: MenuModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID_MENU, menu.id)
        values.put(COLUMN_NAMA_MENU, menu.name)
        values.put(COLUMN_PRICE_MENU, menu.price)

        // prepare image
        val byteOutputStream = ByteArrayOutputStream()
        menu.image.compress(Bitmap.CompressFormat.JPEG, 100, byteOutputStream)
        val imageInByte = byteOutputStream.toByteArray()
        values.put(COLUMN_IMAGE, imageInByte)

        val result = db.insert(TABLE_MENU, null, values)

        // show message
        if (result == (0).toLong()) {
            Toast.makeText(context, "Add Menu Failed", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Add Menu Success", Toast.LENGTH_SHORT).show()
        }

        db.close()
    }
}

