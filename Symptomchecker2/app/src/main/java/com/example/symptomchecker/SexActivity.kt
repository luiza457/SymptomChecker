package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SexActivity: AppCompatActivity() {

    //declare attributes
    private lateinit var sexGroup: RadioGroup;
    private lateinit var sexBtn: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sex)

        //get data from intent
        val old: Intent = intent
        val age: Int = old.getIntExtra("age", -1)

        //set attributes
        sexGroup = findViewById<RadioGroup>(R.id.sexGroup)
        sexBtn = findViewById<Button>(R.id.next)

        //start next phase iff a sex was chosen, else show a toast message
        sexBtn.setOnClickListener {
            //get selected option
            val sex: Int = sexGroup.checkedRadioButtonId
            if (sex == -1) {
                Toast.makeText(this, "Please select your sex!", Toast.LENGTH_SHORT).show()
            } else {
                val i: Intent = Intent(this, RegionActivity::class.java)
                i.putExtra("age", age)
                i.putExtra("sex", if (sex == R.id.male) "Male" else "Female")
                startActivity(i)
                finish() //to avoid having too many activities open at once
            }
        }
    }

    //reopen previous activity when going back
    override fun onBackPressed() {
        val i: Intent = Intent(this,AgeActivity::class.java)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}