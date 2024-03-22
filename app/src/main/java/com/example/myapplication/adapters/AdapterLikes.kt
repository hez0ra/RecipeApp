package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ActivityRecipe
import com.example.myapplication.helpers.OnLikeClickListener
import com.example.myapplication.R
import com.example.myapplication.Recipe

class AdapterLikes(var items: List<Recipe?>, var context: Context, private val listener: OnLikeClickListener): RecyclerView.Adapter<AdapterLikes.MyViewHolder>() {

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.likes_recycler_item_name)
        val time: TextView = view.findViewById(R.id.likes_recycler_item_time)
        val kcal: TextView = view.findViewById(R.id.likes_recycler_item_kcal)
        val serv: TextView = view.findViewById(R.id.likes_recycler_item_serv)
        val btnLike: ImageButton = view.findViewById(R.id.likes_recycler_item_like)
        val layout: LinearLayout = view.findViewById(R.id.likes_recycler_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_likes_recycler, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.name.text = items[pos]?.name
        holder.kcal.text = items[pos]?.kcal.toString()
        holder.time.text = items[pos]?.time.toString()
        holder.serv.text = items[pos]?.serv.toString()

        holder.name.setOnClickListener {
            val intent = Intent(context, ActivityRecipe::class.java)
            intent.putExtra("id", items[pos]!!.id)
            context.startActivity(intent)
        }

        // Получаем массив байтов изображения
        val imageByteArray = items[pos]?.image

        // Преобразуем массив байтов в Drawable
        val yourImageDrawable = BitmapDrawable(context.resources, BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray!!.size))

        // Получаем ссылку на ресурс, представляющий слой layer-list
        val layerDrawable = ContextCompat.getDrawable(context, R.drawable.white_foreground) as? LayerDrawable

        // Устанавливаем нужное изображение в элемент layer-list
        layerDrawable?.setDrawableByLayerId(R.id.white_foreground_img, yourImageDrawable)

        // Устанавливаем измененный Drawable в качестве фона для макета элемента
        holder.layout.background = layerDrawable

        holder.btnLike.setOnClickListener {
            listener.onLikeClicked(items[pos]!!.id, true)
        }

    }
}