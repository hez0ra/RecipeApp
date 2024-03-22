package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.User
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete

class AdapterViewUsers(var items: List<User>, var context: Context, private val listener: Delete) : RecyclerView.Adapter<AdapterViewUsers.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val img: ImageView = view.findViewById(R.id.view_users_ava)
        val userName: TextView = view.findViewById(R.id.view_users_username)
        val delete: ImageButton = view.findViewById(R.id.view_users_btn_delete)
        val email: TextView = view.findViewById(R.id.view_users_email)
        val isAdmin: TextView = view.findViewById(R.id.view_users_isadmin)
        val setAdmin: Button = view.findViewById(R.id.view_users_btn_admin)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewUsers.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_users, parent,false)
        return AdapterViewUsers.MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AdapterViewUsers.MyViewHolder, pos: Int) {

        val dbHelper = DbHelper(context, null)

        holder.userName.text = "Username: " + items[pos].userName
        holder.email.text = "Email: " + items[pos].email
        holder.isAdmin.text = "Admin: " + dbHelper.adminCheck(items[pos].userID).toString()

        Glide.with(context)
            .load(items[pos].image)
            .into(holder.img)

        if(ActiveUser.getIsAdmin()!!){
            holder.setAdmin.visibility = View.VISIBLE
        }
        else{
            holder.setAdmin.visibility = View.GONE
        }

        holder.delete.setOnClickListener {
            listener.onDeleteClick(items[pos].userID)
        }

        holder.setAdmin.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(context)

            // Создаем CheckBox
            val checkBox = CheckBox(context)
            checkBox.text = "Управление админами"

            dialogBuilder.setNegativeButtonIcon(ContextCompat.getDrawable(context, R.drawable.cancel))

            // Добавляем CheckBox в диалог
            dialogBuilder.setView(checkBox)

            // Добавляем кнопку "Подтвердить"
            dialogBuilder.setPositiveButton("Подтвердить") { dialog, which ->
                val dbHelper = DbHelper(context, null)
                dbHelper.addAdmin(items[pos].userID, checkBox.isChecked)
                notifyItemChanged(pos)
            }

            // Устанавливаем иконку закрытия в верхнем правом углу

            // Создаем и отображаем AlertDialog
            val dialog = dialogBuilder.create()
            dialog.show()

        }
    }



}