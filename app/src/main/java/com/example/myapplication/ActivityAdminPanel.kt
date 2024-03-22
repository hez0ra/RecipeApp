package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import com.example.myapplication.helpers.ChangeColor

class ActivityAdminPanel : AppCompatActivity() {

    private var btnAddRecipe: Button? = null
    private var btnViewRecipe: Button? = null
    private var btnViewUsers: Button? = null
    private var btnBack: ImageButton? = null
    private  var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)

        btnAddRecipe = findViewById(R.id.admin_panel_btn_add_recipe)
        btnViewRecipe = findViewById(R.id.admin_panel_btn_view_recipe)
        btnViewUsers = findViewById(R.id.admin_panel_btn_view_users)
        btnBack = findViewById(R.id.admin_panel_back)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK



        btnAddRecipe?.setOnClickListener { toAddRecipe() }
        btnViewRecipe?.setOnClickListener { toViewRecipe() }
        btnViewUsers?.setOnClickListener { toViewUsers() }
        btnBack?.setOnClickListener { finish() }

        // При тёмной теме делать иконки белыми
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.invertColors(btnBack)
        }

        supportActionBar?.hide()
    }

    private fun toAddRecipe(){
        val intent = Intent(this, ActivityAddRecipe::class.java)
        startActivity(intent)
    }

    private fun toViewRecipe(){
        val intent = Intent(this, ActivityViewRecipe::class.java)
        startActivity(intent)
    }

    private fun toViewUsers(){
        val intent = Intent(this, ActivityViewUsers::class.java)
        startActivity(intent)
    }
}