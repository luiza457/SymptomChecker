package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AgeActivity : AppCompatActivity() {

    //declare attributes
    private lateinit var ageInput: EditText
    private lateinit var ageBtn: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.age)

        //initialize attributes
        ageInput = findViewById<EditText>(R.id.ageInput);
        ageBtn = findViewById<Button>(R.id.next);

        //start next phase iff an age was inputted, else show a toast message
        ageBtn.setOnClickListener{
            if (ageInput.text.toString().equals("")){
                Toast.makeText(this, "Please input your age!", Toast.LENGTH_SHORT).show()
            }
            else{
                val age: Int = ageInput.text.toString().toInt()
                val i: Intent = Intent(this, SexActivity::class.java)
                i.putExtra("age", age)
                startActivity(i)
                finish() //to avoid having too many activities open at once
            }


        }
    }

    //reopen previous activity when going back
    override fun onBackPressed() {
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}