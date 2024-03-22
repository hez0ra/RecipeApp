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
import de.hdodenhof.circleimageview.CircleImageView

class AdapterViewUsers(private var items: List<User>, var context: Context, private val listener: Delete) : RecyclerView.Adapter<AdapterViewUsers.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val img: CircleImageView = view.findViewById(R.id.view_users_ava)
        val userName: TextView = view.findViewById(R.id.view_users_username)
        val delete: ImageButton = view.findViewById(R.id.view_users_btn_delete)
        val email: TextView = view.findViewById(R.id.view_users_email)
        val isAdmin: TextView = view.findViewById(R.id.view_users_is_admin)
        val attachToCreate: TextView = view.findViewById(R.id.view_users_attach_to_create)
        val setAdmin: Button = view.findViewById(R.id.view_users_btn_admin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_users, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        val dbHelper = DbHelper(context, null)

        holder.userName.text = "Username: ${items[pos].userName}"
        holder.email.text = "Email: ${items[pos].email}"
        holder.isAdmin.text = "Admin: ${dbHelper.adminCheck(items[pos].userID)}"
        holder.attachToCreate.text = "Attach to create: ${dbHelper.attachToCreateCheck(items[pos].userID)}"

        Glide.with(context)
            .load(items[pos].image)
            .into(holder.img)

        if(ActiveUser.getUserId() == items[pos].userID){
            holder.setAdmin.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.userName.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(items[pos].userID == 1){
            holder.delete.visibility = View.GONE
            holder.setAdmin.visibility = View.GONE
        }
        else if(ActiveUser.getIsAdmin() && ActiveUser.getAttachToCreate()){
            holder.setAdmin.visibility = View.VISIBLE
        }
        else{
            holder.setAdmin.visibility = View.GONE
        }

        holder.delete.setOnClickListener {
            listener.onDeleteClick(items[pos].userID)
        }

        holder.setAdmin.setOnClickListener {
            val inflater = LayoutInflater.from(context)
            val dialogView = inflater.inflate(R.layout.dialog_admin, null)

            val dialogCheckBox: CheckBox = dialogView.findViewById(R.id.dialog_admin_check)
            val btnDialogClose: ImageButton = dialogView.findViewById(R.id.dialog_admin_cansel)
            val btnDialogConfirm: Button = dialogView.findViewById(R.id.dialog_admin_confirm)

            val dialogBuilder = AlertDialog.Builder(context).apply {
                setView(dialogView)
            }

            val dialog = dialogBuilder.create()
            dialog.show()

            btnDialogClose.setOnClickListener { dialog.dismiss() }
            btnDialogConfirm.setOnClickListener {
                val dbHelper = DbHelper(context, null)
                dbHelper.addAdmin(items[pos].userID, dialogCheckBox.isChecked)
                notifyItemChanged(pos)
            }
        }
    }



}