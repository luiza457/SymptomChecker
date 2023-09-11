package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultActivity: AppCompatActivity() {

    //declare attribute
    private lateinit var diseaseText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result)

        //get intent data
        val old: Intent = intent
        val diseases: String? = old.getStringExtra("diseases")

        //initialize attribute
        diseaseText = findViewById<TextView>(R.id.diseases)

        //set text to screen
        diseaseText.text = diseases
    }

    //reopen previous activity when going back
    override fun onBackPressed() {

        //get intent data
        val old: Intent = intent
        val age: Int = old.getIntExtra("age",-1)
        val sex: String? = old.getStringExtra("sex")
        val region: String? = old.getStringExtra("region")
        val symptoms: ArrayList<String> = old.getStringArrayListExtra("symptoms") as ArrayList<String>

        val i: Intent = Intent(this, RisksActivity::class.java)
        i.putExtra("age", age)
        i.putExtra("sex", sex)
        i.putExtra("region", region)
        i.putStringArrayListExtra("symptoms", symptoms)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}