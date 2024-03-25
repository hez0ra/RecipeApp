package com.example.myapplication.helpers

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import java.io.ByteArrayOutputStream
import java.io.IOException


object ImageHelper {

    fun blurBackground(context: Context, blur: BlurView){
        val radius = 16f
        val window = (context as Activity).window
        val decorView = window.decorView
        val rootView = decorView.findViewById<ViewGroup>(android.R.id.content)

        val windowBackground = decorView.background

        blur.setupWith(rootView, RenderScriptBlur(context))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)
    }

    fun translate(string: String): String{
        when(string){
            "Куриное яйцо" -> return "egg_chicken"
            "Чеснок" -> return "garlic"
            "Лимонный сок" -> return "lemon_juice"
            "Перепелиное яйцо" -> return "egg_quail"
            "Оливковое масло" -> return "oil_olive"
            "Креветки" -> return "shrimps"
            "Пармезан", "Твердый сыр", "Сыр грюйер" -> return "parmesan"
            "Горчица" -> return "mustard"
            "Багет" -> return "baguette"
            "Вустерский соус" -> return "sauce_worcestershire"
            "Помидор" -> return "tomato"
            "Анчоусы" -> return "anchovies"
            "Томаты черри" -> return "tomato_cherry"
            "Сахар" -> return "sugar"
            "Пшеничная мука" -> return "flavor_wheat"
            "Соль" -> return "salt"
            "Молоко" -> return "milk"
            "Кукурузный крахмал" -> return "starch_corn"
            "Ванильный сахар" -> return "sugar_vanilla"
            "9% столовый уксус", "Уксус", "Столовый уксус" -> return "vinegar"
            "82,5% масло сливочное", "Сливочное масло", "Масло сливочное" -> return "butter"
            "20% сметана", "Сметана", "Майонез", "Соус бешамель" -> return "sour_cream"
            "Растительное масло", "Подсолнечное масло" -> return "oil"
            "Репчатый лук", "Лук" -> return "onion"
            "Картофель" -> return "potato"
            "Черный молотый перец", "Молотый черный перец", "Свежемолотый черный перец" -> return "pepper_ground_black"
            "Чеддер" -> return "cheddar"
            "Мясной фарш", "Говяжий фарш", "Свиной фарш" -> return "meat_chopped"
            "Листья салата" -> return "salad"
            "Вода" -> return "water"
            "Кабачок" -> return "eggplant"
            "Баклажан", "Цукини" -> return "zucchini"
            "Огурец" -> return "cucumber"
            "Базилик", "Зелень", "Сушеный базилик" -> return "basil"
            "Сушеный орегано" -> return "oregano_powder"
            "Куркума" -> return "curcuma"
            "Рыба", "Филе трески", "Семга"  -> return "fish"
            "Свежая мята" -> return "mint"
            "Спрайт" -> return "sprite"
            "Лайм" -> return "lime"
            "Лед" -> return "ice"
            "Тростниковый сахар", "Коричневый сахар" -> return "sugar_cane"
            "Сладкий перец" -> return "sweet_pepper"
            "Петрушка" -> return "parsley"
            "Тимьян" -> return "thyme"
            "Лавровый лист" -> return "bay_leaf"
            "Морковь" -> return "carrot"
            "Маринованные огурцы", "Соленые огурцы" -> return "cucumber_jar"
            "Зеленый горошек" -> return "pea"
            "Укроп" -> return "dill"
            "Говяжья грудинка", "Говяжья вырезка", "Говядина", "Телячий фарш" -> return "cow"
            "Свекла" -> return "beet"
            "Капуста" -> return "cabbage"
            "Томатная паста", "Помидоры в собственном соку", "Томатный соус" -> return "tomato_sauce"
            "Орегано" -> return "oregano"
            "Моцарелла", "Сыр моцарелла" -> return "mozzarella"
            "Тесто для пиццы" -> return "dough"
            "Сырокопченная колбаса" -> return "salami"
            "Перец чили" -> return "pepper_chilie"
            "Перловая крупа" -> return "cereal"
            "Темный шоколад", "Шоколад" -> return "chocolate"
            "Грецкие орехи" -> return "walnuts"
            "Сухие дрожжи" -> return "dry_yeast"
            "Красное сухое вино", "Вино", "Белон вино", "Красное вино" -> return "wine"
            "Яблоко" -> return "apple"
            "Апельсин" -> return "orange"
            "Корица" -> return "cinnamon"
            "Гвоздика" -> return "carnation"
            "Кардамон" -> return "cardamom"
            "Мускатный орех" -> return "nutmeg"
            "Имбирь" -> return "ginger"
            "Каркаде" -> return "hibiscus"
            "Черный чай" -> return "tea"
            "Зеленый лук" -> return "green_onion"
            "Мед" -> return "honey"
            "Анис", "Бадьян", "Анис (бадьян)", "Анис(бадьян)" -> return "anise"
            "Сливочный сыр" -> return "cream_cheese"
            "Лаваш", "Пита", "Армянский лаваш" -> return "pita"
            "Листья лазаньи", "Готовые листья лазаньи", "Сухие листья лазаньи", "Готовые сухие листья лазаньи" -> return "lazagna_sheets"
            "Докторская колбаса" -> return "doctor_sausage"
            "Курица", "Куриная грудка", "Куриное филе", "Филе курицы", "Грудка курицы" -> return "chicken"
            "Карри", "Соус карри", "Кари", "Соус кари" -> return "kari"
            "Макароны", "Спагетти" -> return "pasta_package"
            "Бульон", "Мясной бульон", "Куриный бульон", "Говяжий бульон", "Свиной бульон" -> return "bouillon"
            else -> return "load"
        }
    }

    fun compressImage(context: Context, drawableId: Int): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(context.resources, drawableId, this)
        }

        val originalSize = options.outWidth * options.outHeight
        val targetSize = 256 * 1024 // 256 КБ в байтах
        val quality = if (originalSize > targetSize) (targetSize.toDouble() / originalSize.toDouble() * 100).toInt() else 100

        (ContextCompat.getDrawable(context, drawableId) as? BitmapDrawable)?.bitmap?.compress(
            Bitmap.CompressFormat.JPEG, quality, outputStream)

        return outputStream.toByteArray()
    }

    fun compressImage(drawable: Drawable): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val bitmap = (drawable as? BitmapDrawable)?.bitmap

        if (bitmap != null) {
            val targetSize = 256 * 1024 // 256 КБ в байтах
            var quality = 100 // Начальное значение качества

            // Бинарный поиск оптимального значения качества
            var low = 0
            var high = 100
            while (low <= high) {
                val mid = (low + high) / 2
                bitmap.compress(Bitmap.CompressFormat.JPEG, mid, outputStream)
                val size = outputStream.size()
                if (size <= targetSize) {
                    quality = mid
                    low = mid + 1
                } else {
                    high = mid - 1
                }
                outputStream.reset()
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        }

        return outputStream.toByteArray()
    }

    fun compressImage(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val targetSize = 256 * 1024 // 256 КБ в байтах
        var quality = 100 // Начальное значение качества

        // Бинарный поиск оптимального значения качества
        var low = 0
        var high = 100
        while (low <= high) {
            val mid = (low + high) / 2
            bitmap.compress(Bitmap.CompressFormat.JPEG, mid, outputStream)
            val size = outputStream.size()
            if (size <= targetSize) {
                quality = mid
                low = mid + 1
            } else {
                high = mid - 1
            }
            outputStream.reset()
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

        return outputStream.toByteArray()
    }


    fun getCircularImage(drawable: Drawable): Bitmap {
        // Преобразование Drawable в Bitmap
        val bitmap = (drawable as BitmapDrawable).bitmap

        val size = bitmap.width.coerceAtMost(bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)

        // Создание нового пустого Bitmap заданного размера и конфигурации ARGB_8888
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // Рисование круга на Canvas
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Обрезка оригинального изображения по кругу
        val srcPaint = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        canvas.drawBitmap(squaredBitmap, 0f, 0f, srcPaint)

        return output
    }

    fun getCircularImage(bitmap: Bitmap): Bitmap {
        val size = bitmap.width.coerceAtMost(bitmap.height)
        val x = (bitmap.width - size) / 2
        val y = (bitmap.height - size) / 2
        val squaredBitmap = Bitmap.createBitmap(bitmap, x, y, size, size)

        // Создание нового пустого Bitmap заданного размера и конфигурации ARGB_8888
        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        // Рисование круга на Canvas
        val paint = Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
        }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)

        // Обрезка оригинального изображения по кругу
        val srcPaint = Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
        canvas.drawBitmap(squaredBitmap, 0f, 0f, srcPaint)

        return output
    }


    fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        return (drawable as BitmapDrawable).bitmap
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun getDefaultAvatar(context: Context) : Drawable{
        val drawableId = R.drawable.ava
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return drawable!!
    }

}