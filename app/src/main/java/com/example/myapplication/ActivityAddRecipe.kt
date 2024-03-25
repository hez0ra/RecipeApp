package com.example.myapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.adapters.AdapterAddRecipeIngr
import com.example.myapplication.adapters.AdapterAddRecipeInstruction
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper

class ActivityAddRecipe : AppCompatActivity(), Delete {

    private var ingredients: RecyclerView? = null
    private var instruction: RecyclerView? = null
    private var category: Spinner? = null
    private var btnAddIngredients: ImageButton? = null
    private var btnAddInstructions: ImageButton? = null
    private var btnConfirm: Button? = null
    private var btnCansel: Button? = null
    private var name:EditText? = null
    private var kcal:EditText? = null
    private var time:EditText? = null
    private var serv:EditText? = null
    private var ingredientsName: EditText? = null
    private var ingredientsAmount: EditText? = null
    private var instrText: EditText? = null
    private var img: ImageView? = null
    private var arrayOfIngredients = arrayListOf<String>()
    private var arrayOfInstructions = arrayListOf<String>()
    private  var currentNightMode: Int = -1
    val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { img?.setImageURI(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)

        ingredients = findViewById(R.id.add_recipe_recycler)
        category = findViewById(R.id.add_recipe_category)
        instruction = findViewById(R.id.add_recipe_instruction_recycler)
        btnAddIngredients = findViewById(R.id.add_recipe_btn_ingr)
        btnAddInstructions = findViewById(R.id.add_recipe_btn_instr)
        btnConfirm = findViewById(R.id.add_recipe_btn_confirm)
        btnCansel = findViewById(R.id.add_recipe_btn_cansel)
        ingredientsName = findViewById(R.id.add_recipe_ingr_name)
        ingredientsAmount = findViewById(R.id.add_recipe_ingr_amount)
        name = findViewById(R.id.add_recipe_name)
        kcal = findViewById(R.id.add_recipe_kcal)
        time = findViewById(R.id.add_recipe_time)
        serv = findViewById(R.id.add_recipe_serv)
        img = findViewById(R.id.add_recipe_img)
        instrText = findViewById(R.id.add_recipe_instruction_input)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngredients, this, this)
        instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstructions, this, this)

        btnAddIngredients?.setOnClickListener { addIngr() }
        btnAddInstructions?.setOnClickListener { addInstr() }
        img?.setOnClickListener { addImage() }
        btnConfirm?.setOnClickListener { addRecipe() }
        btnCansel?.setOnClickListener { finish() }

        // При тёмной теме делать иконки белыми
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            ChangeColor.invertColors(btnAddIngredients)
            ChangeColor.invertColors(btnAddInstructions)
        }

        checkIfEdit()

        supportActionBar?.hide()
        category?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addIngr(){
        val name: String = ingredientsName?.text.toString()
        val amount: String = ingredientsAmount?.text.toString()
        if(name != "" && amount != ""){
            var isAdded = false
            for(item in arrayOfIngredients){
                if(name == item.split("!").first()) isAdded = true
            }

            if(!isAdded){
                arrayOfIngredients.add(name + "!" + amount)
                ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngredients, this, this)
                ingredientsName!!.text.clear()
                ingredientsAmount!!.text.clear()
            }
            else{
                Toast.makeText(this, "Ингредиент с таким названием уже добавлен", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addInstr(){
        val text: String = instrText?.text.toString()
        if(text != ""){
            arrayOfInstructions.add(text)
            instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstructions, this, this)
            instrText!!.text.clear()
        }
        else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addImage(){
        getImageContent.launch("image/*")
    }

    private fun addRecipe(){
        val Name = name?.text.toString()
        val Kcal = kcal?.text.toString()
        val Time = time?.text.toString()
        val Serv = serv?.text.toString()
        val Image = ImageHelper.getBitmapFromDrawable(img!!.drawable)

        if(Name != "" && Kcal != "" && Time != "" && Serv != "" && Image != ImageHelper.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.plus)!!) && arrayOfIngredients.size != 0 && arrayOfInstructions.size != 0){
            Toast.makeText(this, "Рецепт успешно добавлен", Toast.LENGTH_SHORT).show()
            val dbHelper = DbHelper(this, null)
            val selectedCategory = when(category?.selectedItem.toString()){
                "Пицца" -> "pizza"
                "Супы" -> "soup"
                "Паста" -> "pasta"
                "Салаты" -> "salad"
                "Рыбные блюда" -> "fish"
                "Овощные блюда" -> "vegetables"
                "Десерты и выпечка" -> "dessert"
                "Напитки и коктейли" -> "cocktail"
                else -> "meat"
            }
            dbHelper.addRecipe(Recipe(Name, Time.toUInt(), Kcal.toUInt(), Serv.toUInt(), arrayOfIngredients, arrayOfInstructions,
                ImageHelper.compressImage(img!!.drawable), selectedCategory))
            name!!.text.clear()
            kcal!!.text.clear()
            time!!.text.clear()
            serv!!.text.clear()
            arrayOfIngredients.clear()
            arrayOfInstructions.clear()
            ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngredients, this, this)
            instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstructions, this, this)
            img?.setImageResource(R.drawable.plus)
        }
        else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveChanges(recipeID: Int){
        val Name = name?.text.toString()
        val Kcal = kcal?.text.toString()
        val Time = time?.text.toString()
        val Serv = serv?.text.toString()
        val Image = ImageHelper.getBitmapFromDrawable(img!!.drawable)

        if(Name != "" && Kcal != "" && Time != "" && Serv != "" && Image != ImageHelper.getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.plus)!!) && arrayOfIngredients.size != 0 && arrayOfInstructions.size != 0){
            Toast.makeText(this, "nice", Toast.LENGTH_SHORT).show()
            val dbHelper = DbHelper(this, null)
            val selectedCategory = when(category?.selectedItem.toString()){
                "Пицца" -> "pizza"
                "Супы" -> "soup"
                "Паста" -> "pasta"
                "Салаты" -> "salad"
                "Рыбные блюда" -> "fish"
                "Овощные блюда" -> "vegetables"
                "Десерты и выпечка" -> "dessert"
                "Напитки и коктейли" -> "cocktail"
                else -> "meat"
            }
            dbHelper.editRecipe(recipeID, Recipe(Name, Time.toUInt(), Kcal.toUInt(), Serv.toUInt(), arrayOfIngredients, arrayOfInstructions,
                ImageHelper.compressImage(img!!.drawable), selectedCategory))
            finish()
        }
        else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkIfEdit(){
        val id = intent.getIntExtra("id", -1)
        if(id != -1){
            val dbHelper = DbHelper(this, null)
            val recipe = dbHelper.getRecipeById(id)
            name?.setText(recipe?.name)
            time?.setText(recipe?.time.toString())
            kcal?.setText(recipe?.kcal.toString())
            serv?.setText(recipe?.serv.toString())
            category?.setSelection(when(recipe?.category){
                "pizza" -> 1
                "soup" -> 2
                "pasta" -> 3
                "salad" -> 4
                "fish" -> 5
                "vegetables" -> 6
                "dessert" -> 7
                "cocktail" -> 8
                else -> 0
            })
            arrayOfIngredients = ArrayList(recipe?.ingridients!!)
            arrayOfInstructions = ArrayList(recipe?.instruction!!)
            ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngredients, this, this)
            instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstructions, this, this)
            Glide.with(this)
                .load(recipe?.image)
                .into(img!!)
            btnConfirm?.text = "Сохранить изменения"
            btnConfirm?.setOnClickListener { saveChanges(recipe?.id!!) }
        }
    }

    override fun onDeleteClick(position: Int, variant: Int) {
        when(variant){
            0 -> {
                arrayOfIngredients.removeAt(position)
                ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngredients, this, this)
                return
            }
            1 -> {
                arrayOfInstructions.removeAt(position)
                instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstructions, this, this)
                return
            }
            else -> return
        }

    }

    override fun onDeleteClick(userID: Int) {
        return
    }
}