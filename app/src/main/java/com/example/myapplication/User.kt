package com.example.myapplication

class User(var userName: String, val email: String, var pass: String, val userID: Int, val image: ByteArray?) {

    constructor(userName: String, email: String, pass: String): this(userName, email, pass, -1, null)
}