package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapters.AdapterLikes
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.DbHelper
import com.example.myapplication.helpers.OnLikeClickListener

class FragmentLikes: Fragment(), OnLikeClickListener {

    private var recycler: RecyclerView? = null
    private var message: TextView? = null
    private lateinit var dbHelper: DbHelper
    private  var currentNightMode: Int = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_likes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler = view.findViewById(R.id.likes_recycler)
        message = view.findViewById(R.id.likes_zero)
        dbHelper = DbHelper(requireContext(), null)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        val adapter = AdapterLikes(dbHelper.getLike(ActiveUser.getUserId()!!), requireContext(), this)
        recycler?.adapter = adapter
        if(adapter.itemCount == 0){
            message?.visibility = View.VISIBLE
        }
    }

    override fun onLikeClicked(recipeId: Int, isLiked: Boolean) {
        dbHelper.removeLike(ActiveUser.getUserId()!!, recipeId)
        recycler?.adapter = AdapterLikes(dbHelper.getLike(ActiveUser.getUserId()!!), requireContext(), this)
    }

}