package com.example.symptomchecker

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {

    //declare attributes
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var passwordVisibility: CheckBox

    //potential logins
    private val accounts: MutableList<Account> = ArrayList<Account>(listOf(
        Account(0, "Maria Dulgheru", "super_secret_password", "DBA"),
        Account(1, "Jane Doe", "jane_doe_password", "Medic")
    ))


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        //initialize attributes
        usernameInput = findViewById<EditText>(R.id.usernameInput)
        passwordInput = findViewById<EditText>(R.id.passwordInput)
        loginBtn = findViewById<Button>(R.id.login)
        passwordVisibility = findViewById<CheckBox>(R.id.passwordVisibility)

        //login button listener
        loginBtn.setOnClickListener {
            if (usernameInput.text.toString() == "" || passwordInput.text.toString() == ""){
                Toast.makeText(this, "Please enter login credentials", Toast.LENGTH_SHORT).show()
            }
            else{
                var a: Account? = null
                for (account: Account in accounts){
                    if (usernameInput.text.toString().equals(account.getUsername()) &&
                        passwordInput.text.toString().equals(account.getPassword()) ) { //login iff credentials match
                        a = account
                        break
                    }
                }
                if (a != null){
                    val i : Intent = Intent(this, EmployeeActivity::class.java)
                    i.putExtra("username", a.getUsername())
                    i.putExtra("role", a.getRole())
                    startActivity(i)
                    finish() //to avoid having too many activities open at once
                }
                else{
                    Toast.makeText(this, "No matching credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //password visibility checkbox on change listener
        passwordVisibility.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked){
                passwordInput.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            else{
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

    }

    //reopen previous activity when going back
    override fun onBackPressed() {
        val i: Intent = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish() //to avoid having too many activities open at once
    }

    private inner class Account(id: Int, username: String, password: String, role: String){
        private var id: Int = id
        private var username: String = username
        private var password: String = password
        private var role: String = role

        public fun getUsername(): String{
            return username
        }

        public fun getPassword(): String{
            return password
        }

        public fun getRole(): String{
            return role
        }
    }
}