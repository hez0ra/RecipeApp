package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.*
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.ImageHelper
import com.example.myapplication.helpers.OnLikeClickListener
import eightbitlab.com.blurview.BlurView

class AdapterMainRec(var items: List<Recipe>, var context: Context, private val listener: OnLikeClickListener) : RecyclerView.Adapter<AdapterMainRec.MyViewHolder>() {

    private lateinit var dbHelper: DbHelper
    private var listOfLikes = listOf<Recipe?>()
    private val redHeart = R.drawable.heart_red

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.main_recycler_rec_item_name)
        val description: TextView = view.findViewById(R.id.main_recycler_rec_item_desc)
        val image: ImageView = view.findViewById(R.id.main_recycler_rec_item_img)
        val btnLike: ImageButton = view.findViewById(R.id.main_recycler_rec_item_like)
        val blur: BlurView = view.findViewById(R.id.blur)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_recycler_rec, parent, false)
        dbHelper = DbHelper(context, null)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        ImageHelper.blurBackground(context, holder.blur)
        val radiusInPixels = (15 * context.resources.displayMetrics.density).toInt()

        var isLiked = false
        if (ActiveUser.getUser() != null) {
            listOfLikes = dbHelper.getLike(ActiveUser.getUserId()!!)
        }
        for (item in listOfLikes) {
            if (item!!.id == items[pos].id) isLiked = true
        }
        if (isLiked) {
            holder.btnLike.setImageResource(redHeart)
        } else {
            holder.btnLike.setImageResource(R.drawable.heart_white)
        }
        holder.title.text = items[pos].name


        when (items[pos].countIngr) {
            1 -> holder.description.text =
                "${items[pos].countIngr.toString()} ингридиент • ${items[pos].time.toString()} мин"
            2, 3, 4 -> holder.description.text =
                "${items[pos].countIngr.toString()} ингридиента • ${items[pos].time.toString()} мин"
            else -> holder.description.text =
                "${items[pos].countIngr.toString()} ингридиентов • ${items[pos].time.toString()} мин"
        }

        Glide.with(context)
            .load(items[pos].image)
            .apply(RequestOptions.skipMemoryCacheOf(true)) // Не кэшировать изображение в памяти
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) // Не кэшировать изображение на диске
            .apply(RequestOptions().transforms(CenterCrop(), RoundedCorners(radiusInPixels.toInt())))
            .into(holder.image)

        holder.image.setOnClickListener {
            val intent = Intent(context, ActivityRecipe::class.java)
            intent.putExtra("id", items[pos].id)
            context.startActivity(intent)
        }

        holder.btnLike.setOnClickListener {
            listener.onLikeClicked(items[pos].id, isLiked)
        }


    }



}
