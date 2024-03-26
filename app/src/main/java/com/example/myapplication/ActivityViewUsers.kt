package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterViewUsers
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.Delete
import com.example.myapplication.helpers.ImageHelper

class ActivityViewUsers : AppCompatActivity(), Delete {

    private var recycler: RecyclerView? = null
    private var btnBack: ImageButton? = null
    private lateinit var dbHelper: DbHelper
    private var arrayOfUsers = arrayListOf<User>()
    private  var currentNightMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_users)

        recycler = findViewById(R.id.view_users_recycler)
        btnBack = findViewById(R.id.view_users_back)

        dbHelper = DbHelper(this, null)
        arrayOfUsers = dbHelper.getAllUsers()
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        recycler?.adapter = AdapterViewUsers(arrayOfUsers, this, this)

        btnBack?.setOnClickListener { finish() }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ImageHelper.invertColors(btnBack)
        }

        supportActionBar?.hide()
    }

    override fun onDeleteClick(position: Int, variant: Int) {
        return
    }

    override fun onDeleteClick(userID: Int) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setMessage("Вы точно хотите удалить этого пользователя НАВСЕГДА?")
            setPositiveButton("Подтвердить") { dialog, _ ->
                dbHelper.deleteUser(userID)
                arrayOfUsers = dbHelper.getAllUsers()
                recycler?.adapter = AdapterViewUsers(arrayOfUsers, this@ActivityViewUsers, this@ActivityViewUsers)
                dialog.dismiss()
            }
            setNegativeButton("Отмена") { dialog, _ ->
                // Отмена удаления пользователя
                dialog.dismiss()
            }
            setCancelable(false) // Пользователь не может закрыть диалог, нажав вне его
        }
        val dialog = builder.create()
        dialog.show()
    }
}