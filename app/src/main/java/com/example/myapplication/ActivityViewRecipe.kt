package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterViewRecipes
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper

class ActivityViewRecipe : AppCompatActivity(), Delete {

    private var btnBack: ImageButton? = null
    private var recycler: RecyclerView? = null
    private var input: EditText? = null
    private var dbHelper: DbHelper? = null
    private var adapter: AdapterViewRecipes? = null
    private var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        btnBack = findViewById(R.id.view_recipes_back)
        recycler = findViewById(R.id.view_recipes_recycler)
        input = findViewById(R.id.view_recipes_input)
        dbHelper = DbHelper(this, null)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this, this)
        adapter!!.filter.filter(input?.text.toString())
        recycler?.adapter = adapter

        input?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter!!.filter.filter(s.toString())
            }
        })

        btnBack?.setOnClickListener { finish() }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ImageHelper.invertColors(btnBack)
        }

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this, this)
        adapter!!.filter.filter(input?.text.toString())
        recycler?.adapter = adapter
    }

    override fun onDeleteClick(position: Int, variant: Int) {
        return
    }

    override fun onDeleteClick(userID: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Вы точно хотите удалить этот рецепт НАВСЕГДА?")
            setPositiveButton("Подтвердить") { dialog, _ ->
                dbHelper?.deleteRecipe(userID)
                adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this@ActivityViewRecipe, this@ActivityViewRecipe)
                adapter!!.filter.filter(input?.text.toString())
                recycler?.adapter = adapter
                dialog.dismiss()
            }
            setNegativeButton("Отмена") { dialog, _ ->
                // Отмена удаления пользователя
                dialog.dismiss()
            }
            setCancelable(false) // Пользователь не может закрыть диалог, нажав вне его
        }
        val dialog = builder.create()
        dialog.show()
    }

}