package com.example.symptomchecker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SymptomsActivity: AppCompatActivity() {

    //declare attributes
    private lateinit var symptomInput: EditText
    private lateinit var symptomBtn: Button
    private lateinit var symptomList: RecyclerView
    private lateinit var submitBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.symptoms)

        //get data from intent
        val old: Intent = intent
        val age: Int = old.getIntExtra("age",-1)
        val sex: String? = old.getStringExtra("sex")
        val region: String? = old.getStringExtra("region")

        //initialize attributes
        symptomInput = findViewById<EditText>(R.id.symptomInput)
        symptomBtn = findViewById<Button>(R.id.addBtn)
        symptomList = findViewById<RecyclerView>(R.id.symptomList)
        submitBtn = findViewById<Button>(R.id.next)

        //RecyclerView attributes
        symptomList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val symptomListAdapter: SymptomListAdapter = SymptomListAdapter()
        symptomList.adapter = symptomListAdapter

        //add symptom if input not null else show toast
        symptomBtn.setOnClickListener{
            if (symptomInput.text.toString().equals("")){
                Toast.makeText(this, "Please type in a symptom", Toast.LENGTH_SHORT).show()
            }
            else{//check to not add duplicates
                var isDuplicate: Boolean = false
                for (s: String in symptomListAdapter.getSymptoms()){ //check with all symptoms of the adapter
                    if (symptomInput.text.toString().equals(s,ignoreCase = true)){ //case insensitive
                        isDuplicate = true
                        Toast.makeText(this, "Symptom already inputted", Toast.LENGTH_SHORT).show()
                        break;
                    }
                }
                if (!isDuplicate) {
                    symptomListAdapter.addSymptom(symptomInput.text.toString().lowercase().replaceFirstChar { it.titlecase(Locale.getDefault()) })
                }
                symptomInput.setText("")
            }
        }

        //start next phase if at least one symptom given else show toast message.
        submitBtn.setOnClickListener{
            if(symptomListAdapter.itemCount == 0){
                Toast.makeText(this, "Please input at least one symptom", Toast.LENGTH_SHORT).show()
            }
            else{
                val i: Intent = Intent(this, RisksActivity::class.java)
                i.putExtra("age", age)
                i.putExtra("sex", sex)
                i.putExtra("region", region)
                i.putStringArrayListExtra("symptoms", symptomListAdapter.getSymptoms() as ArrayList<String>)
                startActivity(i)
                finish() //to avoid having too many activities open at once
            }
        }
    }


    //reopen previous activity when going back
    override fun onBackPressed() {
        //get data from intent
        val old: Intent = intent
        val age: Int = old.getIntExtra("age",-1)
        val sex: String? = old.getStringExtra("sex")

        val i: Intent = Intent(this, RegionActivity::class.java)
        i.putExtra("age", age)
        i.putExtra("sex", sex)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }

    private inner class SymptomListAdapter : RecyclerView.Adapter<SymptomListAdapter.SymptomViewHolder>() {
        private val symptoms: MutableList<String> = ArrayList()
        public fun addSymptom(s: String) {
            symptoms.add(s)
            notifyItemInserted(this.itemCount - 1)
        }

        public fun deleteSymptom(pos: Int) {
            symptoms.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, itemCount) // Update the item positions
        }

        public fun getSymptoms(): List<String> {
            return symptoms
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
            val ctx: Context = parent.context
            val li: LayoutInflater = LayoutInflater.from(ctx)

            //inflate new view
            val symptomView: View = li.inflate(R.layout.symptom, parent, false)

            //create new view holder
            return SymptomViewHolder(symptomView)
        }

        override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
            val symptom: String = symptoms[position]
            val txtView: TextView = holder.getSymptom()
            txtView.text = symptom
        }

        override fun getItemCount(): Int {
            return symptoms.size
        }

        //Class for extending RecyclerView List item holder
        private inner class SymptomViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            private val symptom: TextView
            private val delBtn: Button

            public fun getSymptom(): TextView{
                return symptom
            }

            init {
                symptom = itemView.findViewById<TextView>(R.id.symptomName)
                delBtn = itemView.findViewById<Button>(R.id.delBtn)

                //on click, delete the element
                delBtn.setOnClickListener{
                    val position = adapterPosition
                    deleteSymptom(position)
                }
            }
        }
    }
}