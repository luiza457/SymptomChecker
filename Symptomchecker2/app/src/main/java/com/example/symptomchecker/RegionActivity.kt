package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class RegionActivity: AppCompatActivity() {

    //declare attributes
    private lateinit var regionSpinner: Spinner
    private lateinit var regionBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.region)

        //get data from intent
        val old : Intent = intent
        val age: Int = old.getIntExtra("age", -1)
        val sex: String? = old.getStringExtra("sex")

        //initialize attributes
        regionSpinner = findViewById<Spinner>(R.id.regionList)
        regionBtn = findViewById<Button>(R.id.next)

        //Spinner options
        val options: MutableList<String> = ArrayList()

        options.add("North America")
        options.add("Central America")
        options.add("South America")
        options.add("North Europe")
        options.add("East Europe")
        options.add("South Europe")
        options.add("West Europe")
        options.add("North Africa")
        options.add("East Africa")
        options.add("South Africa")
        options.add("West Africa")
        options.add("Central Africa")
        options.add("Middle East")
        options.add("India")
        options.add("Far East")
        options.add("Pacific")

        //create an array adapter with the options and set it for the spinner (dropdown list)

        val optionsAdapter: ArrayAdapter<String> = ArrayAdapter(this,R.layout.spinner_item, options)
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        regionSpinner.adapter = optionsAdapter

        //start next phase with chosen region (default will be first option in our list (North America) )
        regionBtn.setOnClickListener{
            val region: String = regionSpinner.selectedItem.toString()
            val i: Intent = Intent(this, SymptomsActivity::class.java)
            i.putExtra("age", age)
            i.putExtra("sex", sex)
            i.putExtra("region", region)
            startActivity(i)
            finish()//to avoid having too many activities open at once
        }
    }

    //reopen previous activity when going back
    override fun onBackPressed() {

        //get data from intent
        val old : Intent = intent
        val age: Int = old.getIntExtra("age", -1)


        val i: Intent = Intent(this, SexActivity::class.java)
        i.putExtra("age", age)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}