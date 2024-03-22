package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterSearch
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.OnLikeClickListener

class ActivitySearch : AppCompatActivity(), OnLikeClickListener {

    private var recycler: RecyclerView? = null
    private var btn_search: ImageButton? = null
    private var input: EditText? = null
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recycler = findViewById(R.id.search_recycler)
        btn_search = findViewById(R.id.search_btn)
        input = findViewById(R.id.search_input)
        dbHelper = DbHelper(this, null)

        val adapter: AdapterSearch
        when(intent.getStringExtra("variant")){
            "all" ->  adapter = AdapterSearch(dbHelper.getAllRecipes(), this, this)
            "pizza" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("pizza"), this, this)
            "pasta" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("pasta"), this, this)
            "meat" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("meat"), this, this)
            "soup" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("soup"), this, this)
            "fish" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("fish"), this, this)
            "vegetables" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("vegetables"), this, this)
            "dessert" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("dessert"), this, this)
            "cocktail" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("cocktail"), this, this)
            "salad" -> adapter = AdapterSearch(dbHelper.getRecipesByCategory("salad"), this, this)
            else -> adapter = AdapterSearch(dbHelper.getAllRecipes(), this, this)
        }
        recycler?.adapter = adapter
        input?.setText(intent.getStringExtra("input"))
        adapter.filter.filter(input?.text.toString())

        input?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }
        })


        supportActionBar?.hide()
    }

    override fun onLikeClicked(recipeId: Int, isLiked: Boolean) {
        if(ActiveUser.getUser() != null){
            if(isLiked){
                dbHelper.removeLike(ActiveUser.getUserId()!!, recipeId)
            }
            else{
                dbHelper.addLike(ActiveUser.getUserId()!!, recipeId)
            }
            var itemsRec: List<Recipe>
            when(intent.getStringExtra("variant")){
                "all" ->  itemsRec = dbHelper.getAllRecipes()
                "pizza" -> itemsRec = dbHelper.getAllRecipes()
                "pasta" -> itemsRec =dbHelper.getRecipesByCategory("pasta")
                "meat" -> itemsRec = dbHelper.getRecipesByCategory("meat")
                "soup" -> itemsRec = dbHelper.getRecipesByCategory("soup")
                "fish" -> itemsRec = dbHelper.getRecipesByCategory("fish")
                "vegetables" -> itemsRec =dbHelper.getRecipesByCategory("vegetables")
                "dessert" -> itemsRec =dbHelper.getRecipesByCategory("dessert")
                "cocktail" -> itemsRec =dbHelper.getRecipesByCategory("cocktail")
                "salad" -> itemsRec =dbHelper.getRecipesByCategory("salad")
                else -> itemsRec = dbHelper.getAllRecipes()
            }

            itemsRec = itemsRec.filter { it.name.contains(input!!.text, ignoreCase = true) }

            // Найдите позицию элемента в вашем списке, который нужно обновить
            val positionToUpdate = itemsRec.indexOfFirst { it.id == recipeId }

            // Если позиция найдена, обновите только этот элемент
            if (positionToUpdate != -1) {
                recycler?.adapter?.notifyItemChanged(positionToUpdate)
            }
        }
        else{
            Toast.makeText(this, "Сначала зайдите в аккаунт", Toast.LENGTH_LONG).show()
        }
    }
}