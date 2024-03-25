package com.example.myapplication

import android.graphics.text.LineBreaker
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.TooltipCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.adapters.AdapterRecipe
import com.example.myapplication.helpers.*
import eightbitlab.com.blurview.BlurView


class ActivityRecipe : AppCompatActivity(), OnLikeClickListener {

    // Объявление переменных
    private var recyclerIngredients: RecyclerView? = null
    private var btnBack: ImageButton? = null
    private var btnLike: ImageButton? = null
    private var mainImg: ImageView? = null
    private var kcalImg: ImageView? = null
    private var minutesImg: ImageView? = null
    private var servImg: ImageView? = null
    private var textMain: TextView? = null
    private var countIngredients: TextView? = null
    private var minutes: TextView? = null
    private var kcal: TextView? = null
    private var serv: TextView? = null
    private var instruction: TextView? = null
    private var amountLikes: TextView? = null
    private var recipe: Recipe? = null
    private var backBlur: BlurView? = null
    private var likeBlur: BlurView? = null
    private lateinit var dbHelper: DbHelper
    private val redHeart = R.drawable.heart_red
    private  var currentNightMode: Int = -1

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        // Инициализация переменных
        recyclerIngredients = findViewById(R.id.recipe_recycler_ingredients)
        btnBack = findViewById(R.id.recipe_back)
        btnLike = findViewById(R.id.recipe_like)
        mainImg = findViewById(R.id.recipe_img_main)
        kcalImg = findViewById(R.id.recipe_img_kcal)
        minutesImg = findViewById(R.id.recipe_img_time)
        servImg = findViewById(R.id.recipe_img_serv)
        textMain = findViewById(R.id.recipe_name)
        countIngredients = findViewById(R.id.recipe_undertitle)
        minutes = findViewById(R.id.recipe_text_time)
        kcal = findViewById(R.id.recipe_text_kcal)
        serv = findViewById(R.id.recipe_text_serv)
        instruction = findViewById(R.id.recipe_text_instruction)
        backBlur = findViewById(R.id.recipe_back_blur)
        likeBlur = findViewById(R.id.recipe_like_blur)
        amountLikes = findViewById(R.id.recipe_like_count)
        dbHelper = DbHelper(this, null)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        ImageHelper.blurBackground(this, backBlur!!)
        ImageHelper.blurBackground(this, likeBlur!!)

        // Получение объекта класса Recipe и заполнение полей
        recipe = dbHelper.getRecipeById(intent.getIntExtra("id", 1))
        textMain?.text = recipe?.name
        when(recipe?.countIngr){
            1 -> countIngredients?.text ="${recipe?.countIngr.toString()} ингредиент"
            2,3,4 -> countIngredients?.text ="${recipe?.countIngr.toString()} ингредиента"
            else -> countIngredients?.text ="${recipe?.countIngr.toString()} ингредиентов"
        }
        var instructionBuff = ""
        var i = 1
        for(item: String in recipe?.instruction!!){
            instructionBuff += i.toString() + ". " + item + "\n\n"
            i++
        }
        instruction?.text = instructionBuff
        minutes?.text = recipe?.time.toString() + " мин"
        kcal?.text = recipe?.kcal.toString() + " ккал"
        when(recipe?.serv){
            1u -> serv?.text ="${recipe?.serv.toString()} порция"
            2u,3u,4u -> serv?.text ="${recipe?.serv.toString()} порции"
            else -> serv?.text ="${recipe?.serv.toString()} порций"
        }
        val radiusInPixels = (20 * this.resources.displayMetrics.density).toInt()
        Glide.with(this)
            .load(recipe?.image)
            .apply(RequestOptions.skipMemoryCacheOf(true)) // Не кэшировать изображение в памяти
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) // Не кэшировать изображение на диске
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusInPixels)))
            .into(mainImg!!)
        val ingr: List<String> = recipe!!.ingridients
        recyclerIngredients?.adapter = AdapterRecipe(ingr, this)

        amountLikes?.text = dbHelper.amountLikes(recipe!!.id).toString()

        btnBack?.setOnClickListener {
            finish()
        }

        kcalImg?.setOnClickListener {
            TooltipCompat.setTooltipText(kcalImg!!, "Калорий в одной порции")
        }
        kcal?.setOnClickListener {
            TooltipCompat.setTooltipText(kcal!!, "Калорий в одной порции")
        }

        // Cистема лайков
        var isLiked = false
        var listOfLikes = listOf<Recipe?>()
        if (ActiveUser.getUser() != null){
            listOfLikes = dbHelper.getLike(ActiveUser.getUserId()!!)
        }
        for (item in listOfLikes){
            if(item!!.id == recipe!!.id) isLiked = true
        }
        btnLike?.setOnClickListener {
                onLikeClicked(recipe!!.id, isLiked)
        }
        if(isLiked){
            btnLike?.setImageResource(redHeart)
        }

        // Выравнивание по ширине текctа
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            instruction?.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }

        // При тёмной теме делать иконки белыми
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(kcalImg)
            ChangeColor.makeImageWhite(minutesImg)
            ChangeColor.makeImageWhite(servImg)
            ChangeColor.invertColors(btnBack)
        }
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
        }
        recreate()
    }
}