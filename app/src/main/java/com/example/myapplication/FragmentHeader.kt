package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.helpers.ActiveUser

class FragmentHeader : Fragment() {
    private var accIcon: ImageView? = null
    private var greeting: TextView? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accIcon = view.findViewById(R.id.acc_icon)
        greeting = view.findViewById(R.id.greeting)

        accIcon?.setOnClickListener{ account() }

        if(ActiveUser.getUser() != null) {
            greeting?.text = "Добрый день, ${ActiveUser.getUser()!!.userName}"
            Glide.with(this)
                .load(ActiveUser.getAvatar())
                .apply(RequestOptions.skipMemoryCacheOf(true)) // Не кэшировать изображение в памяти
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) // Не кэшировать изображение на диске
                .into(accIcon!!)
        }
        else{
            greeting?.text = "Добрый день"
        }
    }

    override fun onResume() {
        super.onResume()

        if(ActiveUser.getUser() != null) {
            greeting?.text = "Добрый день, ${ActiveUser.getUser()!!.userName}"
            Glide.with(this)
                .load(ActiveUser.getAvatar())
                .apply(RequestOptions.skipMemoryCacheOf(true)) // Не кэшировать изображение в памяти
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) // Не кэшировать изображение на диске
                .into(accIcon!!)
        }
        else{
            greeting?.text = "Добрый день"
            Glide.with(this)
                .load(R.drawable.ava)
                .into(accIcon!!)
        }
    }

    private fun account(){
        lateinit var intent: Intent
        if (ActiveUser.getUser() != null) intent = Intent(activity, ActivityProfile::class.java)
        else intent = Intent(activity, ActivityAuthorization::class.java)
        startActivity(intent)
    }
}
