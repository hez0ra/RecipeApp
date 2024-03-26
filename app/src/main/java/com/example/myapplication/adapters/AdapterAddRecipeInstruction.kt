package com.example.myapplication.adapters

import android.content.Context
import android.graphics.text.LineBreaker
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper

class AdapterAddRecipeInstruction(private var items: List<String>, var context: Context, private val listener: Delete) : RecyclerView.Adapter<AdapterAddRecipeInstruction.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val text: TextView = view.findViewById(R.id.add_recipe_instruction_text)
        val delete: ImageButton = view.findViewById(R.id.add_recipe_instruction_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_add_recipe_instruction, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {
        holder.text.text = (pos + 1).toString() + ". " + items[pos]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.text.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        }
        val currentNightMode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ImageHelper.invertColors(holder.delete)
        }

        holder.delete.setOnClickListener {
            listener.onDeleteClick(pos, 1)
        }
    }
}