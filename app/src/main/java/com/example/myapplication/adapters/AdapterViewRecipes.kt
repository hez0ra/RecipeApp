package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ActivityAddRecipe
import com.example.myapplication.ActivityRecipe
import com.example.myapplication.R
import com.example.myapplication.Recipe
import com.example.myapplication.helpers.ImageHelper
import com.example.myapplication.helpers.Delete

class AdapterViewRecipes(private var items: List<Recipe>, var context: Context, private val listener: Delete): RecyclerView.Adapter<AdapterViewRecipes.MyViewHolder>(), Filterable {

    private var filteredList: List<Recipe> = items

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView = view.findViewById(R.id.view_recipe_name)
        val time: TextView = view.findViewById(R.id.view_recipe_item_time)
        val kcal: TextView = view.findViewById(R.id.view_recipe_item_kcal)
        val serv: TextView = view.findViewById(R.id.view_recipe_item_serv)
        val btnEdit: ImageButton = view.findViewById(R.id.view_recipe_btn_edit)
        val btnDelete: ImageButton = view.findViewById(R.id.view_recipe_btn_delete)
        val layout: LinearLayout = view.findViewById(R.id.view_recipe_layout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_recipe, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.name.text = filteredList[pos].name
        holder.time.text = filteredList[pos].time.toString()
        holder.kcal.text = filteredList[pos].kcal.toString()
        holder.serv.text = filteredList[pos].serv.toString()
        holder.btnDelete.setOnClickListener{listener.onDeleteClick(filteredList[pos].id)}
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, ActivityAddRecipe::class.java)
            intent.putExtra("id", filteredList[pos].id)
            context.startActivity(intent)
        }

        val currentNightMode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ImageHelper.invertColors(holder.btnDelete)
            ImageHelper.invertColors(holder.btnEdit)
        }

        holder.name.setOnClickListener {
            val intent = Intent(context, ActivityRecipe::class.java)
            intent.putExtra("id", filteredList[pos].id)
            context.startActivity(intent)
        }

        // Получаем массив байтов изображения
        val imageByteArray = filteredList[pos].image

        // Преобразуем массив байтов в Drawable
        val yourImageDrawable = BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size))

        // Получаем ссылку на ресурс, представляющий слой layer-list
        val layerDrawable = ContextCompat.getDrawable(context, R.drawable.white_foreground) as? LayerDrawable

        // Устанавливаем нужное изображение в элемент layer-list
        layerDrawable?.setDrawableByLayerId(R.id.white_foreground_img, yourImageDrawable)

        // Устанавливаем измененный Drawable в качестве фона для макета элемента
        holder.layout.background = layerDrawable
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().trim()
                filteredList = if (charString.isEmpty()) {
                    items
                } else {
                    val filteredList = items.filter { it.name.contains(charString, ignoreCase = true) }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<Recipe>
                notifyDataSetChanged()
            }
        }
    }

}