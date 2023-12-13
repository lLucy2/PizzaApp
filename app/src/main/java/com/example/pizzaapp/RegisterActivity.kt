package com.example.pizzaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        //instance text
        val txtEmail: EditText = findViewById(R.id.registerEmail)
        val txtName: EditText = findViewById(R.id.registerPersonName)
        val txtLevel: EditText = findViewById(R.id.registerLevel)
        val txtPassword: EditText = findViewById(R.id.registerPassword)

        //instance button Register
        val btnRegister: Button = findViewById(R.id.buttonRegisterAccount)
        val btnHome: Button = findViewById(R.id.buttonHome)

        btnRegister.setOnClickListener {
            val databaseHelper = DatabaseHelper(this)

            val email:String = txtEmail.text.toString().trim()
            val name:String = txtName.text.toString().trim()
            val level:String = txtLevel.text.toString().trim()
            val password:String = txtPassword.text.toString().trim()

            val data:String = databaseHelper.checkData(email)

            if (data == ""){
            databaseHelper.addAccount(
                email, name, level, password)

                val intentLogin = Intent(this@RegisterActivity,
                    LoginActivity::class.java)
                startActivity(intentLogin)

            } else {
                Toast.makeText(
                    this@RegisterActivity,
                    "Register Failed, Your Email Already Exist",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnHome.setOnClickListener{
            val intentHome = Intent(this@RegisterActivity, MainActivity::class.java)
            startActivity(intentHome)
        }

    }
}