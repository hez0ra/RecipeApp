package com.example.myapplication.adapters

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.R
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class AdapterAddRecipeIngr(private var items: List<String>, var context: Context, private val listener: Delete) : RecyclerView.Adapter<AdapterAddRecipeIngr.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val mainImg: ImageView = view.findViewById(R.id.add_recipe_recycler_img)
        val name: TextView = view.findViewById(R.id.add_recipe_recycler_name)
        val amount: TextView = view.findViewById(R.id.add_recipe_recycler_amount)
        val delete: ImageButton = view.findViewById(R.id.add_recipe_recycler_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_recipe_ingredients, parent, false)
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

        val roundedCornersTransformation = RoundedCornersTransformation(radiusInPixels, 0)

        val multiTransformation: Transformation<Bitmap> = MultiTransformation(
            CenterCrop(), // Применить центральное обрезание
            roundedCornersTransformation // Применить закругленные углы
        )

        Glide.with(context)
            .load(resourceId)
            .apply(RequestOptions.bitmapTransform(multiTransformation))
            .into(holder.mainImg)

        holder.delete.setOnClickListener {
            listener.onDeleteClick(pos, 0)
        }
    }


}