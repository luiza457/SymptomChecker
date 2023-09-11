package com.example.symptomchecker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button

class MainActivity : AppCompatActivity() {

    //declare attributes for buttons
    private lateinit var regularBtn: Button
    private lateinit var specialBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize attributes
        regularBtn = findViewById<Button>(R.id.regularUser)
        specialBtn = findViewById<Button>(R.id.specialUser)

        //add onClick listeners for the buttons
        regularBtn.setOnClickListener{
            val i: Intent = Intent(this, AgeActivity::class.java)
            startActivity(i)
            finish() //to avoid having too many activities open at once
        }

        specialBtn.setOnClickListener{
            val i: Intent = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish() //to avoid having too many activities open at once
        }
    }
}