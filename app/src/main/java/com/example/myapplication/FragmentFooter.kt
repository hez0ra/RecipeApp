package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.myapplication.helpers.ActiveUser
import com.example.myapplication.helpers.ChangeColor
import com.example.myapplication.helpers.SharedPreferencesManager

class FragmentFooter: Fragment() {

    // Объявление переменных
    private var btnTheme: ImageButton? = null
    private var btnHome: ImageButton? = null
    private var btnLikes: ImageButton? = null
    private var btnAdmin: ImageButton? = null
    private  var currentNightMode: Int = -1


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_footer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация переменных
        btnTheme = view.findViewById(R.id.main_theme_switch)
        btnHome = view.findViewById(R.id.main_navbar_home)
        btnLikes = view.findViewById(R.id.main_navbar_like)
        btnAdmin = view.findViewById(R.id.main_navbar_admin)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        // При тёмной теме делать иконки белыми
        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.invertColors(btnHome)
            ChangeColor.invertColors(btnLikes)
            ChangeColor.invertColors(btnAdmin)
        }

        // Обработка нажатий
        btnLikes?.setOnClickListener { toLikes() }
        btnHome?.setOnClickListener { toHome() }
        btnTheme?.setOnClickListener { toggleNightMode() }
        btnAdmin?.setOnClickListener { toAdminPanel() }


        if(ActiveUser.getIsAdmin()){
            btnAdmin?.visibility = View.VISIBLE
        }
        else{
            btnAdmin?.visibility = View.GONE
        }

    }

    override fun onResume() {
        super.onResume()
        if(ActiveUser.getIsAdmin()){
            btnAdmin?.visibility = View.VISIBLE
        }
        else{
            btnAdmin?.visibility = View.GONE
        }
    }


    private fun toggleNightMode() {
        val sharedPreferencesManager = SharedPreferencesManager(requireContext())
        when (currentNightMode) {
            android.content.res.Configuration.UI_MODE_NIGHT_NO -> {
                // Если текущая тема - светлая, включить темную
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPreferencesManager.saveTheme("dark")
                activity?.recreate() // Пересоздать активити для применения изменений
            }
            android.content.res.Configuration.UI_MODE_NIGHT_YES -> {
                // Если текущая тема - темная, включить светлую
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPreferencesManager.saveTheme("light")
                activity?.recreate() // Пересоздать активити для применения изменений
            }
        }


    }

    private fun toLikes(){
        if(ActiveUser.getUser() != null) {
            val fragmentManager = requireActivity().supportFragmentManager

            // Создаем транзакцию фрагментов
            val transaction = fragmentManager.beginTransaction()

            // Заменяем текущий главный фрагмент на новый
            val newFragment = FragmentLikes() // Замените YourNewMainFragment на ваш фрагмент
            transaction.replace(R.id.main_container, newFragment)

            // Закрываем транзакцию
            transaction.commit()
        }
    }

    private fun toHome(){
        val fragmentManager = requireActivity().supportFragmentManager

        // Создаем транзакцию фрагментов
        val transaction = fragmentManager.beginTransaction()

        // Заменяем текущий главный фрагмент на новый
        val newFragment = FragmentHome() // Замените YourNewMainFragment на ваш фрагмент
        transaction.replace(R.id.main_container, newFragment)

        // Закрываем транзакцию
        transaction.commit()
    }

    private fun toAdminPanel(){
        val  intent = Intent(requireContext(), ActivityAdminPanel::class.java)
        startActivity(intent)
    }
}