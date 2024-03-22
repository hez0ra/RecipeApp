package com.example.myapplication.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.ActivitySearch
import com.example.myapplication.R
import com.example.myapplication.Section

class AdapterMainSection(var items: List<Section>, var context: Context) : RecyclerView.Adapter<AdapterMainSection.MyViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.section_topic)
        val image: ImageView = view.findViewById(R.id.img_section)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic_recycler, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count();
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.title.text = items[position].name
        val radiusInPixels = (25 * context.resources.displayMetrics.density).toInt()
        val resourceId = context.resources.getIdentifier(items[position].src, "drawable", context.packageName)
        Glide.with(context)
            .load(resourceId)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusInPixels)))
            .into(holder.image)

        holder.image.setOnClickListener {
            val intent = Intent(context, ActivitySearch::class.java)
            intent.putExtra("variant", items[position].src.removePrefix("topic_"))
            context.startActivity(intent)
        }
    }

}