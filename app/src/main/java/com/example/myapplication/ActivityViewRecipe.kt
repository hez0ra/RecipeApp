package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityViewRecipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        supportActionBar?.hide()
    }
}