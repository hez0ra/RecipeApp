package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterViewRecipes
import com.example.myapplication.adapters.AdapterViewUsers
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete

class ActivityViewRecipe : AppCompatActivity(), Delete {

    private var btnBack: ImageButton? = null
    private var recycler: RecyclerView? = null
    private var dbHelper: DbHelper? = null
    private var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_recipe)

        btnBack = findViewById(R.id.view_recipes_back)
        recycler = findViewById(R.id.view_recipes_recycler)
        dbHelper = DbHelper(this, null)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        recycler?.adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this, this)

        btnBack?.setOnClickListener { finish() }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.invertColors(btnBack)
        }

        supportActionBar?.hide()
    }

    override fun onResume() {
        super.onResume()
        recycler?.adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this, this)
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
                recycler?.adapter = AdapterViewRecipes(dbHelper?.getAllRecipes()!!, this@ActivityViewRecipe, this@ActivityViewRecipe)
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