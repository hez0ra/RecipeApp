package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterSearch
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.OnLikeClickListener

class ActivitySearch : AppCompatActivity(), OnLikeClickListener {

    private var recycler: RecyclerView? = null
    private var btnSearch: ImageButton? = null
    private var input: EditText? = null
    private var noResult: TextView? = null
    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        recycler = findViewById(R.id.search_recycler)
        btnSearch = findViewById(R.id.search_btn)
        input = findViewById(R.id.search_input)
        noResult = findViewById(R.id.search_no_result)
        dbHelper = DbHelper(this, null)

        input?.setText(intent.getStringExtra("input"))
        val adapter: AdapterSearch = when(intent.getStringExtra("variant")){
            "all" -> AdapterSearch(dbHelper.getAllRecipes(), this, this)
            "pizza" -> AdapterSearch(dbHelper.getRecipesByCategory("pizza"), this, this)
            "pasta" -> AdapterSearch(dbHelper.getRecipesByCategory("pasta"), this, this)
            "meat" -> AdapterSearch(dbHelper.getRecipesByCategory("meat"), this, this)
            "soup" -> AdapterSearch(dbHelper.getRecipesByCategory("soup"), this, this)
            "fish" -> AdapterSearch(dbHelper.getRecipesByCategory("fish"), this, this)
            "vegetables" -> AdapterSearch(dbHelper.getRecipesByCategory("vegetables"), this, this)
            "dessert" -> AdapterSearch(dbHelper.getRecipesByCategory("dessert"), this, this)
            "cocktail" -> AdapterSearch(dbHelper.getRecipesByCategory("cocktail"), this, this)
            "salad" -> AdapterSearch(dbHelper.getRecipesByCategory("salad"), this, this)
            else -> AdapterSearch(dbHelper.getAllRecipes(), this, this)
        }
        adapter.filter.filter(input?.text.toString())
        recycler?.adapter = adapter

        input?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filter.filter(s.toString())
            }
        })

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkResult(adapter.itemCount)
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
            var itemsRec: List<Recipe> = when(intent.getStringExtra("variant")){
                "all" -> dbHelper.getAllRecipes()
                "pizza" -> dbHelper.getAllRecipes()
                "pasta" -> dbHelper.getRecipesByCategory("pasta")
                "meat" -> dbHelper.getRecipesByCategory("meat")
                "soup" -> dbHelper.getRecipesByCategory("soup")
                "fish" -> dbHelper.getRecipesByCategory("fish")
                "vegetables" -> dbHelper.getRecipesByCategory("vegetables")
                "dessert" -> dbHelper.getRecipesByCategory("dessert")
                "cocktail" -> dbHelper.getRecipesByCategory("cocktail")
                "salad" -> dbHelper.getRecipesByCategory("salad")
                else -> dbHelper.getAllRecipes()
            }
            itemsRec = itemsRec.filter { it.name.contains(input!!.text, ignoreCase = true) }
            val positionToUpdate = itemsRec.indexOfFirst { it.id == recipeId }
            if (positionToUpdate != -1) {
                recycler?.adapter?.notifyItemChanged(positionToUpdate)
            }
        }
        else{
            Toast.makeText(this, "Сначала зайдите в аккаунт", Toast.LENGTH_LONG).show()
        }
    }



    private fun checkResult(itemCount: Int){
        if(itemCount == 0){
            noResult?.visibility = View.VISIBLE
            recycler?.visibility = View.GONE
        }
        else{
            noResult?.visibility = View.GONE
            recycler?.visibility = View.VISIBLE
        }
    }

}