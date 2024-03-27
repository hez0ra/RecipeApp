package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class FragmentAdminPanel : Fragment() {

    private var btnAddRecipe: Button? = null
    private var btnViewRecipe: Button? = null
    private var btnViewUsers: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_admin_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddRecipe = view.findViewById(R.id.admin_panel_btn_add_recipe)
        btnViewRecipe = view.findViewById(R.id.admin_panel_btn_view_recipe)
        btnViewUsers = view.findViewById(R.id.admin_panel_btn_view_users)

        btnAddRecipe?.setOnClickListener { toAddRecipe() }
        btnViewRecipe?.setOnClickListener { toViewRecipe() }
        btnViewUsers?.setOnClickListener { toViewUsers() }
    }


    private fun toAddRecipe(){
        val intent = Intent(activity, ActivityAddRecipe::class.java)
        startActivity(intent)
    }

    private fun toViewRecipe(){
        val intent = Intent(activity, ActivityViewRecipe::class.java)
        startActivity(intent)
    }

    private fun toViewUsers(){
        val intent = Intent(activity, ActivityViewUsers::class.java)
        startActivity(intent)
    }
}