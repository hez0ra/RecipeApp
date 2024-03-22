package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.SharedPreferencesManager

class ActivityMain : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragmentHeader = FragmentHeader()
        fragmentTransaction.replace(R.id.header_container, fragmentHeader)

        val mainFragment = FragmentHome()
        fragmentTransaction.replace(R.id.main_container, mainFragment)

        val fragmentFooter = FragmentFooter()
        fragmentTransaction.replace(R.id.footer_container, fragmentFooter)

        fragmentTransaction.commit()

        val sharedPreferencesManager = SharedPreferencesManager(this)
        if(sharedPreferencesManager.getUserID(this) != null){
            val dbHelper = DbHelper(this, null)
            dbHelper.getUser(sharedPreferencesManager.getUserID(this)!!)
        }

        sharedPreferencesManager.applySavedTheme()
        supportActionBar?.hide()
    }

}