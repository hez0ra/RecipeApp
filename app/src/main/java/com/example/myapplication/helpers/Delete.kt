package com.example.myapplication.helpers

interface Delete {
    fun onDeleteClick(position: Int, variant: Int)

    fun onDeleteClick(userID: Int)
}