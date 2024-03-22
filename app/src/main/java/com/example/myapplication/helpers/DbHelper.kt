package com.example.myapplication.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.Recipe
import com.example.myapplication.User

class DbHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?)

    : SQLiteOpenHelper(context, "app", factory, 1) {

    private val defaultAvatar = ImageHelper.compressImage(ImageHelper.getCircularImage(ImageHelper.getDefaultAvatar(context)))

    companion object{
        // Таблица пользователей
        private const val TABLE_USERS = "users"
        private const val USERS_ID = "id"
        private const val USERS_USERNAME =  "username"
        private const val USERS_EMAIL = "email"
        private const val USERS_PASS = "pass"
        private const val USERS_IMAGE = "image"

        // Таблица рецептов
        private const val TABLE_RECIPES = "recipes"
        private const val RECIPES_ID = "id"
        private const val RECIPES_NAME = "name"
        private const val RECIPES_TIME = "time"
        private const val RECIPES_KCAL = "kcal"
        private const val RECIPES_SERV = "serv"
        private const val RECIPES_INGREDIENTS = "ingredients"
        private const val RECIPES_INSTRUCTION = "instruction"
        private const val RECIPES_IMAGE = "image"
        private const val RECIPES_CATEGORY = "category"

        // Таблица лайков
        private const val TABLE_LIKES = "likes"
        private const val LIKES_ID = "id"
        private const val LIKES_USER = "userid"
        private const val LIKES_RECIPE = "recipeid"

        // Таблица админов
        private const val TABLE_ADMINS = "admins"
        private const val ADMINS_ID = "id"
        private const val ADMINS_USER_ID = "user_id"
        private const val ADMINS_ATTACH_CREATE = "attach_create"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        var query = "";
        query = """CREATE TABLE $TABLE_USERS (
            $USERS_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            $USERS_USERNAME TEXT, 
            $USERS_EMAIL TEXT, 
            $USERS_PASS TEXT,
            $USERS_IMAGE BLOB)
            """.trimMargin()
        db!!.execSQL(query)

        query = "INSERT INTO $TABLE_USERS ($USERS_USERNAME, $USERS_EMAIL, $USERS_PASS, $USERS_IMAGE) VALUES (?, ?, ?, ?)"
        val values = arrayOf("admin", "admin@gmail.com", "admin123", defaultAvatar)
        db!!.execSQL(query, values)

        query = """CREATE TABLE $TABLE_RECIPES (
            $RECIPES_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $RECIPES_NAME TEXT,
            $RECIPES_TIME INTEGER,
            $RECIPES_KCAL INTEGER,
            $RECIPES_SERV INTEGER,
            $RECIPES_INGREDIENTS TEXT,
            $RECIPES_INSTRUCTION TEXT,
            $RECIPES_IMAGE BLOB,
            $RECIPES_CATEGORY TEXT)
        """.trimIndent()
        db!!.execSQL(query)
        query= """
            CREATE TABLE $TABLE_LIKES (
            $LIKES_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $LIKES_USER INTEGER,
            $LIKES_RECIPE INTEGER,
            FOREIGN KEY ($LIKES_USER) REFERENCES $TABLE_USERS($USERS_ID) ON DELETE CASCADE,
            FOREIGN KEY ($LIKES_RECIPE) REFERENCES $TABLE_RECIPES($RECIPES_ID) ON DELETE CASCADE)
            """.trimIndent()
        db!!.execSQL(query)
        query= """
            CREATE TABLE $TABLE_ADMINS (
            $ADMINS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $ADMINS_USER_ID INTEGER,
            $ADMINS_ATTACH_CREATE BOOLEAN,
            FOREIGN KEY ($ADMINS_USER_ID) REFERENCES $TABLE_USERS($USERS_ID) ON DELETE CASCADE)
            """.trimIndent()
        db!!.execSQL(query)
        query = """
            INSERT INTO $TABLE_ADMINS ($ADMINS_USER_ID, $ADMINS_ATTACH_CREATE) VALUES (1, true)
        """.trimIndent()
        db!!.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_RECIPES")
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_LIKES")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put(USERS_USERNAME, user.userName)
        values.put(USERS_EMAIL, user.email)
        values.put(USERS_PASS, user.pass)
        values.put(USERS_IMAGE, defaultAvatar)

        val db = this.writableDatabase
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    fun getUser(email: String, pass: String){
        val db = this.readableDatabase
        val result =
            db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $USERS_EMAIL = '$email' OR $USERS_USERNAME = '$email' AND $USERS_PASS = '$pass'", null)
        if (result.moveToFirst()) {
            // Проверяем, существуют ли столбцы в курсоре
            val idIndex = result.getColumnIndex(USERS_ID)
            val usernameIndex = result.getColumnIndex(USERS_USERNAME)
            val emailIndex = result.getColumnIndex(USERS_EMAIL)
            val passIndex = result.getColumnIndex(USERS_PASS)
            val imageIndex = result.getColumnIndex(USERS_IMAGE)
            if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && passIndex != -1 && imageIndex != -1) {
                // Если все столбцы найдены, создаем и возвращаем объект User
                val id = result.getInt(idIndex)
                val username = result.getString(usernameIndex)
                val userEmail = result.getString(emailIndex)
                val userPass = result.getString(passIndex)
                val image = result.getBlob(imageIndex)
                ActiveUser.setUser(User(username, userEmail, userPass, id, image))
                ActiveUser.setUserId(id)
                ActiveUser.setIsAdmin(adminCheck(id))
                if(adminCheck(id)) ActiveUser.setAttachToCreate(attachToCreateCheck(id))
            }
        }

        db.close()
    }

    fun getUser(id: Int){
        val db = this.readableDatabase
        val result =
            db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $USERS_ID = '$id'", null)
        if (result.moveToFirst()) {
            // Проверяем, существуют ли столбцы в курсоре
            val idIndex = result.getColumnIndex(USERS_ID)
            val usernameIndex = result.getColumnIndex(USERS_USERNAME)
            val emailIndex = result.getColumnIndex(USERS_EMAIL)
            val passIndex = result.getColumnIndex(USERS_PASS)
            val imageIndex = result.getColumnIndex(USERS_IMAGE)
            if (idIndex != -1 && usernameIndex != -1 && emailIndex != -1 && passIndex != -1 && imageIndex != -1) {
                // Если все столбцы найдены, создаем и возвращаем объект User
                val id = result.getInt(idIndex)
                val username = result.getString(usernameIndex)
                val userEmail = result.getString(emailIndex)
                val userPass = result.getString(passIndex)
                val image = result.getBlob(imageIndex)
                ActiveUser.setUser(User(username, userEmail, userPass, id, image))
                ActiveUser.setUserId(id)
                ActiveUser.setIsAdmin(adminCheck(id))
                if(adminCheck(id)) ActiveUser.setAttachToCreate(attachToCreateCheck(id))
            }
        }
        db.close()
    }

    fun changePassword(userID: Int, newPassword: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USERS_PASS, newPassword)
        }
        db.update(TABLE_USERS, contentValues, "$USERS_ID = ?", arrayOf(userID.toString()))
        db.close()
    }

    fun changeAvatar(userID: Int, newAvatar: ByteArray){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USERS_IMAGE, newAvatar)
        }
        db.update(TABLE_USERS, contentValues, "$USERS_ID = ?", arrayOf(userID.toString()))
        db.close()
    }

    fun changeUserName(userID: Int, newUserName: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(USERS_USERNAME, newUserName)
        }
        db.update(TABLE_USERS, contentValues, "$USERS_ID = ?", arrayOf(userID.toString()))
        db.close()
    }

    fun getAvatar(userID: Int) : ByteArray?{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $USERS_ID = '$userID'", null)
        if(cursor.moveToFirst()){
            return cursor.getBlob(cursor.getColumnIndexOrThrow(USERS_IMAGE))
        }
        return null
    }

    fun getAllUsers(): ArrayList<User> {
        val usersList = arrayListOf<User>()
        val db = this.readableDatabase
        var offset = 0
        val limit = 10 // Размер порции запроса

        while (true) {
            val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS LIMIT $limit OFFSET $offset", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(USERS_ID))
                    val userName = cursor.getString(cursor.getColumnIndexOrThrow(USERS_USERNAME))
                    val pass = cursor.getString(cursor.getColumnIndexOrThrow(USERS_PASS))
                    val email = cursor.getString(cursor.getColumnIndexOrThrow(USERS_EMAIL))
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow(USERS_IMAGE))
                    usersList.add(User(userName, email, pass, id, image))
                } while (cursor.moveToNext())
            } else {
                break // Прерываем цикл, если больше нет данных
            }
            cursor.close()
            offset += limit // Увеличиваем смещение для следующей порции запроса
        }

        db.close()
        return usersList
    }

    fun deleteUser(userID: Int){
        val db = this.writableDatabase
        val selection = "$USERS_ID = ?"
        val selectionArgs = arrayOf(userID.toString())
        db.delete(TABLE_USERS, selection, selectionArgs)
        db.close()
    }

    fun addAdmin(userID: Int, attachToCreate: Boolean){
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(ADMINS_USER_ID, userID)
            put(ADMINS_ATTACH_CREATE, attachToCreate)
        }
        db.insert(TABLE_ADMINS, null, contentValues)
        db.close()
    }

    fun adminCheck(userID: Int): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADMINS WHERE $ADMINS_USER_ID = '$userID'", null)
        return cursor.moveToFirst()
    }

    fun attachToCreateCheck(userID: Int): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ADMINS WHERE $ADMINS_USER_ID = '$userID'", null)
        if(cursor.moveToFirst()){
            return cursor.getInt(cursor.getColumnIndexOrThrow(ADMINS_ATTACH_CREATE)) != 0
        }
        else return false
    }

    fun addRecipe(recipe: Recipe) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(RECIPES_NAME, recipe.name)
            put(RECIPES_TIME, recipe.time.toInt())
            put(RECIPES_KCAL, recipe.kcal.toInt())
            put(RECIPES_SERV, recipe.serv.toInt())
            put(RECIPES_INGREDIENTS, recipe.ingridients.joinToString("|"))
            put(RECIPES_INSTRUCTION, recipe.instruction.joinToString("|"))
            put(RECIPES_IMAGE, recipe.image)
            put(RECIPES_CATEGORY, recipe.category)
        }
        db.insert(TABLE_RECIPES, null, values)

        db.close()
    }

    fun getAllRecipes(): List<Recipe> {
        val recipeList = mutableListOf<Recipe>()
        val db = this.readableDatabase
        var offset = 0
        val limit = 4 // Размер порции запроса

        while (true) {
            val cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES LIMIT $limit OFFSET $offset", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_NAME))
                    val time = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_TIME)).toUInt()
                    val kcal = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_KCAL)).toUInt()
                    val serv = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_SERV)).toUInt()
                    val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INGREDIENTS)).split("|")
                    val instructions = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INSTRUCTION)).split("|")
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow(RECIPES_IMAGE))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_CATEGORY))

                    recipeList.add(Recipe(id, name, time, kcal, serv, ingredients, instructions, image, category))
                } while (cursor.moveToNext())
            } else {
                break // Прерываем цикл, если больше нет данных
            }
            cursor.close()
            offset += limit // Увеличиваем смещение для следующей порции запроса
        }

        db.close()
        return recipeList
    }

    fun getRecipeById(recipeID: Int): Recipe? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES WHERE $RECIPES_ID = '$recipeID'", null)
        var recipe: Recipe? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_NAME))
            val time = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_TIME))
            val kcal = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_KCAL))
            val serv = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_SERV))
            val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INGREDIENTS)).split("|")
            val instruction = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INSTRUCTION)).split("|")
            val image = cursor.getBlob(cursor.getColumnIndexOrThrow(RECIPES_IMAGE))
            val category = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_CATEGORY))

            recipe = Recipe(recipeID, name, time.toUInt(), kcal.toUInt(), serv.toUInt(), ingredients, instruction, image, category)
        }
        cursor.close()
        db.close()
        return recipe
    }

    fun getRecipesByCategory(category: String) : List<Recipe>{
        val db = this.readableDatabase
        val recipeList = mutableListOf<Recipe>()
        var offset = 0
        val limit = 4 // Размер порции запроса
        while (true) {
            val cursor = db.rawQuery("SELECT * FROM $TABLE_RECIPES WHERE $RECIPES_CATEGORY = '$category' LIMIT $limit OFFSET $offset", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_NAME))
                    val time = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_TIME)).toUInt()
                    val kcal = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_KCAL)).toUInt()
                    val serv = cursor.getInt(cursor.getColumnIndexOrThrow(RECIPES_SERV)).toUInt()
                    val ingredients = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INGREDIENTS)).split("|")
                    val instructions = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_INSTRUCTION)).split("|")
                    val image = cursor.getBlob(cursor.getColumnIndexOrThrow(RECIPES_IMAGE))
                    val category = cursor.getString(cursor.getColumnIndexOrThrow(RECIPES_CATEGORY))

                    recipeList.add(Recipe(id, name, time, kcal, serv, ingredients, instructions, image, category))
                } while (cursor.moveToNext())
            } else {
                break // Прерываем цикл, если больше нет данных
            }
            cursor.close()
            offset += limit // Увеличиваем смещение для следующей порции запроса
        }

        db.close()
        return recipeList
    }

    fun clearTable(tableName: String) {
        val db = writableDatabase
        db.delete(tableName, null, null)
        db.close()
    }

    fun addLike(userID: Int, recipeId: Int){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(LIKES_USER, userID)
        values.put(LIKES_RECIPE, recipeId)
        db.insert(TABLE_LIKES, null, values)
        db.close()
    }

    fun removeLike(userID: Int, recipeId: Int) {
        val db = this.writableDatabase
        val selection = "$LIKES_USER = ? AND $LIKES_RECIPE = ?"
        val selectionArgs = arrayOf(userID.toString(), recipeId.toString())
        db.delete(TABLE_LIKES, selection, selectionArgs)
        db.close()
    }

    fun getLike(userID: Int): List<Recipe?> {
        val recipeList = arrayListOf<Recipe?>()
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM $TABLE_LIKES WHERE $LIKES_USER = ?", arrayOf(userID.toString()))
        while (result.moveToNext()) {
            val recipeID = result.getInt(result.getColumnIndexOrThrow(LIKES_RECIPE))
            val recipe = getRecipeById(recipeID)
            recipeList.add(recipe)
        }
        result.close()
        db.close()
        return recipeList
    }

}