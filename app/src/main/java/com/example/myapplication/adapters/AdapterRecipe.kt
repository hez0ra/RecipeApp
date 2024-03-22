package com.example.myapplication.adapters

import com.example.myapplication.helpers.ImageHelper
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R

class AdapterRecipe(private var items: List<String>, var context: Context) : RecyclerView.Adapter<AdapterRecipe.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val mainImg: ImageView = view.findViewById(R.id.recipe_recycler_ingredients_item_icon)
        val name: TextView = view.findViewById(R.id.recipe_recycler_ingredients_item_name)
        val amount: TextView = view.findViewById(R.id.recipe_recycler_ingredients_item_count)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe_recycler_ingredients, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        val itemsName = arrayListOf<String>()
        val itemsAmount = arrayListOf<String>()
        for(item in items){
            val buff = item.split("!")
            itemsName.add(buff[0])
            itemsAmount.add(buff[1])
        }
        holder.name.text = itemsName[pos]
        holder.amount.text = itemsAmount[pos]
        val radiusInPixels = (20 * context.resources.displayMetrics.density).toInt()
        val resourceId = context.resources.getIdentifier(ImageHelper.translate(itemsName[pos]), "drawable", context.packageName)
        Glide.with(context)
            .load(resourceId)
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radiusInPixels)))
            .into(holder.mainImg)
    }
}