package com.example.myapplication.helpers

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class SharedPreferencesManager(private val context: Context) {

    companion object{
        private const val USER_ID = "user_id"
        private const val USER_DATA = "user_data"
        private const val THEME = "theme"
    }

    fun saveUserID(context: Context, userID: Int) {
        val sharedPreferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(USER_ID, userID.toString())
        editor.apply()
    }

    fun getUserID(context: Context): Int? {
        val sharedPreferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        return sharedPreferences.getString(USER_ID, null)?.toInt()
    }

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear().apply()
    }

    fun saveTheme(theme: String) {
        val sharedPreferences = context.getSharedPreferences(THEME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(THEME, theme)
        editor.apply()
    }

    fun applySavedTheme() {
        val sharedPreferences = context.getSharedPreferences(THEME, Context.MODE_PRIVATE)
        val savedTheme = sharedPreferences.getString(THEME, "light") // По умолчанию - светлая тема
        if (savedTheme == "dark") {
            // Установка тёмной темы
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            // Установка светлой темы
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}
