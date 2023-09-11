package com.example.symptomchecker

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.OutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class RisksActivity: AppCompatActivity() {

    //declare attributes
    private lateinit var heightInput: EditText
    private lateinit var weightInput: EditText
    private lateinit var computeBtn: Button
    private lateinit var resetBtn: Button
    private lateinit var submitBtn: Button
    private lateinit var outputTxt: TextView
    private lateinit var weightGroup: RadioGroup
    private lateinit var underweight: RadioButton
    private lateinit var overweight: RadioButton
    private lateinit var obese: RadioButton
    private lateinit var smoker: CheckBox
    private lateinit var alcoholic: CheckBox
    private lateinit var workaholic: CheckBox
    private val ctx: AppCompatActivity = this //required to preserve the context in the coroutine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.risks)

        //get data from intent
        val old: Intent = intent
        val age: Int = old.getIntExtra("age",-1)
        val sex: String? = old.getStringExtra("sex")
        val region: String? = old.getStringExtra("region")
        val symptoms: ArrayList<String> = old.getStringArrayListExtra("symptoms") as ArrayList<String>

        //initilize attributes
        heightInput = findViewById<EditText>(R.id.heightInput)
        weightInput = findViewById<EditText>(R.id.weightInput)
        computeBtn = findViewById<Button>(R.id.compute)
        resetBtn = findViewById<Button>(R.id.reset)
        submitBtn = findViewById<Button>(R.id.submit)
        outputTxt = findViewById<TextView>(R.id.bmioutput)
        weightGroup = findViewById<RadioGroup>(R.id.weightGroup)
        underweight = findViewById<RadioButton>(R.id.underweight)
        overweight = findViewById<RadioButton>(R.id.overweight)
        obese = findViewById<RadioButton>(R.id.obese)
        smoker = findViewById<CheckBox>(R.id.smoker)
        alcoholic = findViewById<CheckBox>(R.id.alcoholic)
        workaholic = findViewById<CheckBox>(R.id.workaholic)


        //add listener for bmi calculator button. show toast message if missing inputs
        //output value on screen and autoselect the BMI category
        computeBtn.setOnClickListener{
            if (heightInput.text.toString()=="" || weightInput.text.toString()==""){
                Toast.makeText(this, "Missing inputs", Toast.LENGTH_SHORT).show()
            }
            else{
                val BMI: Double = String.format("%.2f",weightInput.text.toString().toDouble() / Math.pow(heightInput.text.toString().toDouble(),2.0)).toDouble()
                outputTxt.text = BMI.toString()
                if (BMI < 18.5){//underweight
                    outputTxt.setTextColor(Color.rgb(255,0,0))
                    underweight.isChecked = true
                    for (i in 0 until weightGroup.childCount){
                        val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                        if (child !== underweight){
                            child.isChecked = false
                            child.isEnabled = false
                        }
                    }
                }
                else if (BMI > 25 && BMI <=30){//overweight
                    outputTxt.setTextColor(Color.rgb(255,0,0))
                    overweight.isChecked = true
                    for (i in 0 until weightGroup.childCount){
                        val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                        if (child !== overweight){
                            child.isChecked = false
                            child.isEnabled = false
                        }
                    }
                }
                else if (BMI > 30){//obese
                    outputTxt.setTextColor(Color.rgb(255,0,0))
                    obese.isChecked = true
                    for (i in 0 until weightGroup.childCount){
                        val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                        if (child !== obese){
                            child.isChecked = false
                            child.isEnabled = false
                        }
                    }
                }
                else{//healthy
                    outputTxt.setTextColor(Color.rgb(100,204,51))
                    for (i in 0 until weightGroup.childCount){
                        val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                        child.isChecked = false
                        child.isEnabled = false
                    }
                }
            }
        }

        //clear texts and reenable clicking on radio buttons
        resetBtn.setOnClickListener {

            heightInput.setText("")
            weightInput.setText("")
            outputTxt.text = ""

            for (i in 0 until weightGroup.childCount){
                val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                child.isChecked = false
                child.isEnabled = true
            }
        }

        //any risk factor is optional, can send data on click
        submitBtn.setOnClickListener {

            // do as a coroutine
            GlobalScope.launch {
                //get the Risk Factors
                val risks: MutableList<String> = ArrayList()

                //start with the checkboxes
                if (smoker.isChecked)
                    risks.add("Smoker")

                if (alcoholic.isChecked)
                    risks.add("Alcoholic")

                if (workaholic.isChecked)
                    risks.add("Workaholic")

                //select the bmi risk category from the group
                for (i in 0 until weightGroup.childCount){
                    val child: RadioButton = weightGroup.getChildAt(i) as RadioButton
                    if (child.isChecked){
                        risks.add(child.text.toString())
                        break
                    }
                }

                //create a JSON object with all of the data from intent plus risk factors
                val json: JSONObject = JSONObject()
                json.put("age", age)
                json.put("sex", sex)
                json.put("region", region)
                json.put("symptoms", symptoms)
                json.put("risks", risks)

                //server url
                val url = URL("http://10.0.2.2:8000") //special ip address to refer to computer when working with android emulator

                var writer: BufferedWriter? = null
                var resMsg: String? = null
                //try to open connection to server
                try {
                    val con = withContext(Dispatchers.IO) {
                        url.openConnection()
                    } as HttpURLConnection
                    con.requestMethod = "POST"
                    con.doOutput = true

                    //request headers
                    con.setRequestProperty("Content-Type", "application/json")
                    con.setRequestProperty("Accept", "application/json")

                    //write data as stream
                    val oStream: OutputStream = con.outputStream
                    writer = BufferedWriter(OutputStreamWriter(oStream))
                    withContext(Dispatchers.IO) {
                        writer.write(json.toString())
                        writer.flush()
                    }

                    //get response from server
                    val resCode = con.responseCode
                    resMsg = con.responseMessage
                }
                catch (e: Exception){ //in case of error
                    e.printStackTrace()

                    //run on UI thread the toast message
                    runOnUiThread {
                        Toast.makeText(ctx, "An error occured trying to send the data to the server", Toast.LENGTH_SHORT).show()
                    }
                }
                finally{ //close writer, optionally start the new activity with the output
                    withContext(Dispatchers.IO) {
                        writer?.close()
                    }

                    if (!resMsg.isNullOrEmpty()){
                        val i: Intent = Intent(ctx, ResultActivity::class.java)
                        //putting all data aside from risks, such that in case we move back from result
                        //to risks activity we preserve all input data without needing to keep open
                        //this activity
                        i.putExtra("age", age)
                        i.putExtra("sex", sex)
                        i.putExtra("region", region)
                        i.putStringArrayListExtra("symptoms", symptoms)
                        i.putExtra("diseases", resMsg)
                        startActivity(i)
                        finish() //to avoid having too many activities open at once
                    }
                }
            }
        }
    }

    //reopen previous activity when going back
    override fun onBackPressed() {

        //get data from intent
        val old: Intent = intent
        val age: Int = old.getIntExtra("age",-1)
        val sex: String? = old.getStringExtra("sex")
        val region: String? = old.getStringExtra("region")

        val i: Intent = Intent(this, SymptomsActivity::class.java)
        i.putExtra("age", age)
        i.putExtra("sex", sex)
        i.putExtra("region", region)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }
}