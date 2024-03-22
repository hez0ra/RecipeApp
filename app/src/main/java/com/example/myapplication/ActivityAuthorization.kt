package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.SharedPreferencesManager

class ActivityAuthorization : AppCompatActivity() {

    private var btnAuth: Button? = null
    private var btnBack: ImageButton? = null
    private var email: EditText? = null
    private var pass: EditText? = null
    private var check: CheckBox? = null
    private var toReg: TextView? = null
    private var drop: TextView? = null
    private var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        btnAuth = findViewById(R.id.btn_auth)
        btnBack = findViewById(R.id.auth_arrow_back)
        email = findViewById(R.id.auth_email)
        pass = findViewById(R.id.auth_pass)
        check = findViewById(R.id.auth_check)
        toReg = findViewById(R.id.to_reg)
        drop = findViewById(R.id.DROPDB)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        drop?.setOnClickListener {
            val dbHelper = DbHelper(this, null)
            dbHelper.clearTable("users")
            dbHelper.clearTable("recipes")
        }

        btnBack?.setOnClickListener{
            finish()
        }
        toReg?.setOnClickListener{
            val intent = Intent(this, ActivityRegistration::class.java)
            startActivity(intent)
        }

        btnAuth?.setOnClickListener {
            val Email: String = email?.text.toString().trim()
            val Pass: String = pass?.text.toString().trim()

            if(Email == "" || Pass == ""){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            }
            else{
                val db = DbHelper(this, null)
                db.getUser(Email, Pass)
                if(ActiveUser.getUser() != null){
                    if(check!!.isChecked){
                        val sharedPreferencesManager = SharedPreferencesManager(this)
                        sharedPreferencesManager.saveUserID(this, ActiveUser.getUserId()!!)
                    }
                    finish()
                }
                else{
                    Toast.makeText(this, "Неправильный email или пароль", Toast.LENGTH_LONG).show()
                    pass!!.text.clear()
                }
            }
        }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(btnBack)
        }

        supportActionBar?.hide()

    }
}