package com.example.pizzaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //hide title bar
        getSupportActionBar()?.hide()

        //instance text
        val txtUsername:EditText = findViewById(R.id.editTextEmail)
        val txtPassword:EditText = findViewById(R.id.editTextPassword)
        //instance button login
        val btnLogin:Button = findViewById(R.id.buttonLogin)
//        val tvRegis:TextView = findViewById(R.id.tv_regis)

        //event button login
        btnLogin.setOnClickListener {
            val dbHelper = DatabseHelper(this)

            //check data
           val data:String = dbHelper.checkData("rayfebriantomasila@students.amikom.ac.id")
            Toast.makeText(this@LoginActivity, "Result : $data", Toast.LENGTH_SHORT).show()

            if (data == null){
                //insert data
                dbHelper.addAcount("rayfebriantomasila@students.amikom.ac.id","Ray Febrian T.","Kasir","12345")
            }

            val email = txtUsername.text.toString().trim()
            val password = txtPassword.text.toString().trim()


            val result:Boolean = dbHelper.checkLogin(txtUsername.text.toString(), txtPassword.text.toString())

            if (result == true) {
                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intentLogin)

            } else {
                Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                txtUsername.hint = "Email"
                txtPassword.hint = "Password"
            }

        }



    }
}