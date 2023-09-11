package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmployeeActivity: AppCompatActivity() {

    //initialize attributes
    private lateinit var welcomeMsg: TextView
    private lateinit var msgBtn: Button
    private lateinit var dbBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.employee)

        //get intent data
        val old: Intent = intent
        val name: String? = old.getStringExtra("username")
        val role: String? = old.getStringExtra("role")

        //initialize attributes
        welcomeMsg = findViewById<TextView>(R.id.welcomeMsg)
        msgBtn = findViewById<Button>(R.id.messenger)
        dbBtn = findViewById<Button>(R.id.db)

        //personalize welcome message
        welcomeMsg.text = welcomeMsg.text.toString() + " " + role + " " + name + "!"

        //add button onclick listeners (features to be added later)
        msgBtn.setOnClickListener {
            Toast.makeText(this, "Messenger functionality under development", Toast.LENGTH_SHORT).show()
        }

        dbBtn.setOnClickListener {
            Toast.makeText(this, "Database access functionality under development", Toast.LENGTH_SHORT).show()
        }
    }

    //reopen previous activity when going back
    override fun onBackPressed() {
        val i: Intent = Intent(this, LoginActivity::class.java)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}