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
import com.example.myapplication.Recipe
import com.example.myapplication.User
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper
import de.hdodenhof.circleimageview.CircleImageView

class AdapterViewUsers(private var items: List<User>, var context: Context, private val listener: Delete) : RecyclerView.Adapter<AdapterViewUsers.MyViewHolder>(), Filterable {

    private var filteredList: MutableList<User> = items.toMutableList()

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val delete: ImageButton = view.findViewById(R.id.view_users_btn_delete)
        val setAdmin: Button = view.findViewById(R.id.view_users_btn_admin)
        val img: CircleImageView = view.findViewById(R.id.view_users_ava)
        val userName: TextView = view.findViewById(R.id.view_users_username)
        val email: TextView = view.findViewById(R.id.view_users_email)
        val isAdmin: TextView = view.findViewById(R.id.view_users_is_admin)
        val attachToCreate: TextView = view.findViewById(R.id.view_users_attach_to_create)
        val id: TextView = view.findViewById(R.id.view_users_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view_users, parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, pos: Int) {

        val dbHelper = DbHelper(context, null)

        holder.userName.text = "Username: ${filteredList[pos].userName}"
        holder.email.text = "Email: ${filteredList[pos].email}"
        holder.isAdmin.text = "Admin: ${dbHelper.adminCheck(filteredList[pos].userID)}"
        holder.attachToCreate.text = "Attach to create: ${dbHelper.attachToCreateCheck(filteredList[pos].userID)}"
        holder.id.text = "ID: ${filteredList[pos].userID}"

        Glide.with(context)
            .load(filteredList[pos].image)
            .into(holder.img)

        val currentNightMode = context.resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ImageHelper.invertColors(holder.delete)
        }

        if(ActiveUser.getUserId() == filteredList[pos].userID){
            holder.setAdmin.visibility = View.GONE
            holder.delete.visibility = View.GONE
            holder.userName.setTextColor(ContextCompat.getColor(context, R.color.red))
        }
        else if(filteredList[pos].userID == 1){
            holder.delete.visibility = View.GONE
            holder.setAdmin.visibility = View.GONE
        }
        else if(dbHelper.adminCheck(filteredList[pos].userID) && ActiveUser.getAttachToCreate()){
            holder.setAdmin.text = "Забрать admin права"
            holder.setAdmin.setOnClickListener { removeAdmin(pos) }
        }
        else if(ActiveUser.getAttachToCreate()){
            holder.setAdmin.visibility = View.VISIBLE
            holder.setAdmin.setOnClickListener {
                setAdmin(pos)
            }
        }
        else{
            holder.setAdmin.visibility = View.GONE
        }

        holder.delete.setOnClickListener {
            listener.onDeleteClick(filteredList[pos].userID)
        }
    }

    fun setAdmin(pos: Int){
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
            dbHelper.addAdmin(filteredList[pos].userID, dialogCheckBox.isChecked)
            notifyItemChanged(pos)
            dialog.dismiss()
        }
    }
    fun removeAdmin(pos: Int){
        val dbHelper = DbHelper(context, null)
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setMessage("Вы точно хотите забрать права администратора у этого пользователя?")
            setPositiveButton("Подтвердить") { dialog, _ ->
                dbHelper.deleteAdmin(filteredList[pos].userID)
                notifyItemChanged(pos)
                dialog.dismiss()
            }
            setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            setCancelable(false) // Пользователь не может закрыть диалог, нажав вне его
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().lowercase().trim()
                val tempList = mutableListOf<User>() // Используем временный список

                if (charString.isEmpty()) {
                    tempList.addAll(items)
                } else {
                    for (item in items) {
                        if (item.userName.lowercase().contains(charString) ||
                            item.email.lowercase().contains(charString) ||
                            item.userID.toString().contains(charString)
                        ) {
                            tempList.add(item)
                        }
                    }
                }

                val filterResults = FilterResults()
                filterResults.values = tempList.toList()
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList.clear() // Очищаем текущий список
                filteredList.addAll(results?.values as List<User>) // Добавляем новые элементы
                notifyDataSetChanged()
            }
        }
    }

}