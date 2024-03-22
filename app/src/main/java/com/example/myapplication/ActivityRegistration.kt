package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.DbHelper

class ActivityRegistration : AppCompatActivity() {

    private var btnConfirm: Button? = null
    private var btnBack: ImageButton? = null
    private var userName: EditText? = null
    private var email: EditText? = null
    private var pass: EditText? = null
    private var pass2: EditText? = null
    private var toAuth: TextView? = null
    private var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        btnConfirm = findViewById(R.id.btn_reg)
        btnBack = findViewById(R.id.reg_arrow_back)
        userName = findViewById(R.id.reg_username)
        email = findViewById(R.id.reg_email)
        pass = findViewById(R.id.reg_pass)
        pass2 = findViewById(R.id.reg_pass2)
        toAuth = findViewById(R.id.to_auth)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        btnBack?.setOnClickListener{
            finish()
        }

        toAuth?.setOnClickListener{
            finish()
        }

        btnConfirm?.setOnClickListener {
            val UserName: String = userName?.text.toString().trim()
            val Email: String = email?.text.toString().trim()
            val Pass: String = pass?.text.toString().trim()
            val Pass2: String = pass2?.text.toString().trim()

            if(UserName == "" || Email == "" || Pass == "" || Pass2 == ""){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            }
            else if(!isValidEmail(Email)){
                email?.error = "Некорректный email"
            }
            else if(Pass != Pass2){
                Toast.makeText(this, "Введенные пароли не совпадают", Toast.LENGTH_LONG).show()
            }
            else{
                val user = User(UserName, Email, Pass)
                val db = DbHelper(this, null)
                db.addUser(user)
                Toast.makeText(this, "Успешная регистрация", Toast.LENGTH_SHORT).show()

                userName!!.text.clear()
                email!!.text.clear()
                pass!!.text.clear()
                pass2!!.text.clear()
            }

        }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(btnBack)
        }

        supportActionBar?.hide()

    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }


}