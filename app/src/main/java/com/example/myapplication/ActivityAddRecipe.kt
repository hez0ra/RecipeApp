package com.example.myapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterAddRecipeIngr
import com.example.myapplication.adapters.AdapterAddRecipeInstruction
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper

class ActivityAddRecipe : AppCompatActivity(), Delete {

    private var ingredients: RecyclerView? = null;
    private var instruction: RecyclerView? = null;
    private var category: Spinner? = null
    private var btnAddIngr: ImageButton? = null;
    private var btnAddInstr: ImageButton? = null;
    private var btnConfirm: Button? = null;
    private var name:EditText? = null;
    private var kcal:EditText? = null;
    private var time:EditText? = null;
    private var serv:EditText? = null;
    private var ingrName: EditText? = null;
    private var ingrAmount: EditText? = null;
    private var instrText: EditText? = null;
    private var img: ImageView? = null;
    private var arrayOfIngr = arrayListOf<String>()
    private var arrayOfInstr = arrayListOf<String>()
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
        btnAddIngr = findViewById(R.id.add_recipe_btn_ingr)
        btnAddInstr = findViewById(R.id.add_recipe_btn_instr)
        btnConfirm = findViewById(R.id.add_recipe_btn_confirm)
        ingrName = findViewById(R.id.add_recipe_ingr_name)
        ingrAmount = findViewById(R.id.add_recipe_ingr_amount)
        name = findViewById(R.id.add_recipe_name)
        kcal = findViewById(R.id.add_recipe_kcal)
        time = findViewById(R.id.add_recipe_time)
        serv = findViewById(R.id.add_recipe_serv)
        img = findViewById(R.id.add_recipe_img)
        instrText = findViewById(R.id.add_recipe_instruction_input)
        currentNightMode =
            resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK


        ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngr, this, this)
        instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstr, this, this)

        btnAddIngr?.setOnClickListener { addIngr() }
        btnAddInstr?.setOnClickListener { addInstr() }
        img?.setOnClickListener { addImage() }
        btnConfirm?.setOnClickListener { addRecipe() }

        // При тёмной теме делать иконки белыми
        if (currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES) {
            ChangeColor.invertColors(btnAddIngr)
            ChangeColor.invertColors(btnAddInstr)
        }

        supportActionBar?.hide()
        category?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun addIngr(){
        val name: String = ingrName?.text.toString()
        val amount: String = ingrAmount?.text.toString()
        if(name != "" && amount != ""){
            var isAdded = false
            for(item in arrayOfIngr){
                if(name == item.split("!").first()) isAdded = true
            }

            if(!isAdded){
                arrayOfIngr.add(name + "!" + amount)
                ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngr, this, this)
                ingrName!!.text.clear()
                ingrAmount!!.text.clear()
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
            arrayOfInstr.add(text)
            instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstr, this, this)
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

        if(Name != "" && Kcal != "" && Time != "" && Serv != "" && Image != ImageHelper.getBitmapFromDrawable(getDrawable(resources.getIdentifier("plus", "drawable", packageName))!!) && arrayOfIngr.size != 0 && arrayOfInstr.size != 0){
            Toast.makeText(this, "nice", Toast.LENGTH_SHORT).show()
            val dbHelper = DbHelper(this, null)
            dbHelper.addRecipe(Recipe(Name, Time.toUInt(), Kcal.toUInt(), Serv.toUInt(), arrayOfIngr, arrayOfInstr,
                ImageHelper.compressImage(img!!.drawable), category?.selectedItem.toString()))
            name!!.text.clear()
            kcal!!.text.clear()
            time!!.text.clear()
            serv!!.text.clear()
            arrayOfIngr.clear()
            arrayOfInstr.clear()
            ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngr, this, this)
            instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstr, this, this)
            img?.setImageResource(resources.getIdentifier("plus", "drawable", packageName))
        }
        else{
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDeleteClick(position: Int, variant: Int) {
        when(variant){
            0 -> {
                arrayOfIngr.removeAt(position)
                ingredients?.adapter = AdapterAddRecipeIngr(arrayOfIngr, this, this)
                return
            }
            1 -> {
                arrayOfInstr.removeAt(position)
                instruction?.adapter = AdapterAddRecipeInstruction(arrayOfInstr, this, this)
                return
            }
            else -> return
        }

    }

    override fun onDeleteClick(userID: Int) {
        return
    }
}