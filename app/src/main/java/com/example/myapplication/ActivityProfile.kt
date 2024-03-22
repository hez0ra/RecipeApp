package com.example.myapplication

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication.helpers.*
import de.hdodenhof.circleimageview.CircleImageView

class ActivityProfile : AppCompatActivity() {
    private var btnChangePassword: Button? = null
    private var btnChangeUserName: Button? = null
    private var btnDialogConfirm: Button? = null
    private var btnBack: ImageButton? = null
    private var btnLogOut: ImageButton? = null
    private var btnDialogClose: ImageButton? = null
    private var dialogOldPass: EditText? = null
    private var dialogNewPass1: EditText? = null
    private var dialogNewPass2: EditText? = null
    private var dialogNewUserName: EditText? = null
    private var ava: CircleImageView? = null
    private var userName: TextView? = null
    private var email: TextView? = null
    private lateinit var user: User
    private var currentNightMode: Int = -1
    private lateinit var dbHelper: DbHelper
    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Получите изображение по URI и передайте его в функцию changeAvatar
            val imageBitmap = ImageHelper.getBitmapFromUri(this, uri)
            imageBitmap?.let { bitmap ->
                dbHelper.changeAvatar(ActiveUser.getUserId()!!, ImageHelper.compressImage(ImageHelper.getCircularImage(bitmap)))
            }
            dbHelper.getUser(ActiveUser.getUserId()!!)
            recreate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.acitivity_profile)

        btnChangePassword = findViewById(R.id.profile_change_password)
        btnChangeUserName = findViewById(R.id.profile_change_username)
        btnBack = findViewById(R.id.profile_back)
        btnLogOut = findViewById(R.id.profile_log_out)
        ava = findViewById(R.id.profile_ava)
        userName = findViewById(R.id.profile_user_name)
        email = findViewById(R.id.profile_email)
        user = ActiveUser.getUser()!!
        dbHelper = DbHelper(this, null)
        currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK

        userName?.text = user.userName
        email?.text = maskEmail(user.email)

        btnBack?.setOnClickListener{ finish() }
        btnLogOut?.setOnClickListener { logOut() }
        btnChangePassword?.setOnClickListener { showChangePasswordDialog(this) }
        btnChangeUserName?.setOnClickListener { showChangeUserNameDialog(this) }


        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(btnLogOut)
            ChangeColor.makeImageWhite(btnBack)
        }

        Glide.with(this)
            .load(ActiveUser.getAvatar())
            .apply(RequestOptions.skipMemoryCacheOf(true)) // Не кэшировать изображение в памяти
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)) // Не кэшировать изображение на диске
            .into(ava!!)
        ava?.setOnClickListener { getImage() }
        supportActionBar?.hide()
    }

    private fun maskEmail(email: String): String{
        val atIndex = email.indexOf('@')
        val maskLength = 4
        val visiblePart = email.substring(0, atIndex).substring(0, minOf(maskLength, email.substring(0, atIndex).length))
        val maskedPart = "●".repeat(maxOf(0, email.substring(0, atIndex).length - maskLength))
        return "$visiblePart$maskedPart${email.substring(atIndex)}"
    }

    private fun logOut(){
        ActiveUser.setUser(null)
        ActiveUser.setUserId(-1)
        ActiveUser.setIsAdmin(false)
        val sharedPreferencesManager = SharedPreferencesManager(this)
        sharedPreferencesManager.logout(this)
        finish()
    }

    private fun showChangePasswordDialog(context: Context){
        val dbHelper = DbHelper(context, null)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_new_password, null)

        btnDialogClose = dialogView.findViewById(R.id.dialog_cansel)
        dialogOldPass = dialogView.findViewById(R.id.dialog_old_password)
        dialogNewPass1 = dialogView.findViewById(R.id.dialog_new_password1)
        dialogNewPass2 = dialogView.findViewById(R.id.dialog_new_password2)
        btnDialogConfirm = dialogView.findViewById(R.id.dialog_confirm)

        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
        }

        val dialog = dialogBuilder.create()
        dialog.show()

        btnDialogClose?.setOnClickListener{ dialog.dismiss() }
        btnDialogConfirm?.setOnClickListener {
            val oldPass: String = dialogOldPass?.text.toString()
            val newPass1: String = dialogNewPass1?.text.toString()
            val newPass2: String = dialogNewPass2?.text.toString()

            if(oldPass == "" || newPass1 == "" || newPass2 == ""){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            }
            else if(oldPass != user.pass){
                dialogOldPass?.error = "Неправильный пароль"
            }
            else if(newPass1 != newPass2){
                dialogNewPass2?.error = "Введеные пароли не совпадают"
            }
            else if(newPass1 == oldPass){
                Toast.makeText(this, "Новый пароль должен отличаться", Toast.LENGTH_LONG).show()
            }
            else{
                dbHelper.changePassword(ActiveUser.getUserId()!!, newPass1)
                user.pass = newPass1
                Toast.makeText(this, "Пароль успешно изменен", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(btnDialogClose)
        }
    }

    private fun showChangeUserNameDialog(context: Context){
        val dbHelper = DbHelper(context, null)
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.dialog_new_username, null)

        dialogNewUserName = dialogView.findViewById(R.id.dialog_new_username)
        btnDialogClose = dialogView.findViewById(R.id.dialog_cansel_username)
        btnDialogConfirm = dialogView.findViewById(R.id.dialog_confirm_username)

        val dialogBuilder = AlertDialog.Builder(context).apply {
            setView(dialogView)
        }

        val dialog = dialogBuilder.create()
        dialog.show()

        btnDialogClose?.setOnClickListener{ dialog.dismiss() }
        btnDialogConfirm?.setOnClickListener {
            val newUserName: String = dialogNewUserName?.text.toString()

            if(newUserName == ""){
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_LONG).show()
            }
            else{
                dbHelper.changeUserName(ActiveUser.getUserId()!!, newUserName)
                Toast.makeText(this, "Имя пользователя успешно изменено", Toast.LENGTH_LONG).show()
                user.userName = newUserName
                ActiveUser.setUser(user)
                dialog.dismiss()
                recreate()
            }
        }

        if(currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES){
            ChangeColor.makeImageWhite(btnDialogClose)
        }
    }
    private fun getImage() {
        getImageContent.launch("image/*")
    }

}