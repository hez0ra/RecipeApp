package com.example.myapplication.helpers

import com.example.myapplication.User

object ActiveUser {
    private var user: User? = null
    private var userId : Int? = null
    private var isAdmin: Boolean = false
    private var attachToCreate: Boolean = false

    fun setUser(user: User?) {
        ActiveUser.user = user
    }
    fun setUserId(id: Int){
        userId = id
    }
    fun getUser(): User?{
        return user
    }
    fun getUserId(): Int?{
        return userId
    }

    fun getIsAdmin(): Boolean{
        return isAdmin
    }

    fun setIsAdmin(isAdmin: Boolean){
        this.isAdmin = isAdmin
    }

    fun getAttachToCreate(): Boolean{
        return attachToCreate
    }

    fun setAttachToCreate(attachToCreate: Boolean){
        this.attachToCreate = attachToCreate
    }

    fun getAvatar() : ByteArray?{
        return user?.image
    }
}