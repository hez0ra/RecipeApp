package com.example.myapplication.helpers

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import android.widget.ImageView
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
        when(string.lowercase()){
            "куриное яйцо", "яйцо" -> return "egg_chicken"
            "чеснок" -> return "garlic"
            "лимонный сок" -> return "lemon_juice"
            "перепелиное яйцо" -> return "egg_quail"
            "оливковое масло" -> return "oil_olive"
            "креветки" -> return "shrimps"
            "пармезан", "твердый сыр", "сыр грюйер", "сыр" -> return "parmesan"
            "горчица" -> return "mustard"
            "багет" -> return "baguette"
            "вустерский соус" -> return "sauce_worcestershire"
            "помидор" -> return "tomato"
            "анчоусы" -> return "anchovies"
            "томаты черри" -> return "tomato_cherry"
            "сахар" -> return "sugar"
            "пшеничная мука" -> return "flavor_wheat"
            "соль" -> return "salt"
            "молоко" -> return "milk"
            "кукурузный крахмал" -> return "starch_corn"
            "ванильный сахар" -> return "sugar_vanilla"
            "9% столовый уксус", "уксус", "столовый уксус" -> return "vinegar"
            "82,5% масло сливочное", "сливочное масло", "масло сливочное" -> return "butter"
            "20% сметана", "сметана", "майонез", "соус бешамель" -> return "sour_cream"
            "растительное масло", "подсолнечное масло" -> return "oil"
            "репчатый лук", "лук" -> return "onion"
            "картофель" -> return "potato"
            "черный молотый перец", "молотый черный перец", "свежемолотый черный перец" -> return "pepper_ground_black"
            "чеддер" -> return "cheddar"
            "мясной фарш", "говяжий фарш", "свиной фарш" -> return "meat_chopped"
            "листья салата" -> return "salad"
            "вода" -> return "water"
            "кабачок" -> return "eggplant"
            "баклажан", "цукини" -> return "zucchini"
            "огурец" -> return "cucumber"
            "базилик", "зелень", "сушеный базилик" -> return "basil"
            "сушеный орегано" -> return "oregano_powder"
            "куркума" -> return "curcuma"
            "рыба", "филе трески", "семга"  -> return "fish"
            "свежая мята" -> return "mint"
            "спрайт" -> return "sprite"
            "лайм" -> return "lime"
            "лед" -> return "ice"
            "тростниковый сахар", "коричневый сахар" -> return "sugar_cane"
            "сладкий перец" -> return "sweet_pepper"
            "петрушка" -> return "parsley"
            "тимьян" -> return "thyme"
            "лавровый лист" -> return "bay_leaf"
            "морковь" -> return "carrot"
            "маринованные огурцы", "соленые огурцы" -> return "cucumber_jar"
            "зеленый горошек" -> return "pea"
            "укроп" -> return "dill"
            "говяжья грудинка", "говяжья вырезка", "говядина", "телячий фарш" -> return "cow"
            "свекла" -> return "beet"
            "капуста" -> return "cabbage"
            "томатная паста", "помидоры в собственном соку", "томатный соус" -> return "tomato_sauce"
            "орегано" -> return "oregano"
            "моцарелла", "сыр моцарелла" -> return "mozzarella"
            "тесто для пиццы", "тесто" -> return "dough"
            "сырокопченная колбаса" -> return "salami"
            "перец чили" -> return "pepper_chilie"
            "перловая крупа" -> return "cereal"
            "темный шоколад", "шоколад" -> return "chocolate"
            "грецкие орехи" -> return "walnuts"
            "сухие дрожжи" -> return "dry_yeast"
            "красное сухое вино", "вино", "белон вино", "красное вино" -> return "wine"
            "яблоко" -> return "apple"
            "апельсин" -> return "orange"
            "корица" -> return "cinnamon"
            "гвоздика" -> return "carnation"
            "кардамон" -> return "cardamom"
            "мускатный орех" -> return "nutmeg"
            "имбирь" -> return "ginger"
            "каркаде" -> return "hibiscus"
            "черный чай" -> return "tea"
            "зеленый лук" -> return "green_onion"
            "мед" -> return "honey"
            "анис", "бадьян", "анис (бадьян)", "анис(бадьян)" -> return "anise"
            "сливочный сыр" -> return "cream_cheese"
            "лаваш", "пита", "армянский лаваш" -> return "pita"
            "листья лазаньи", "готовые листья лазаньи", "сухие листья лазаньи", "готовые сухие листья лазаньи" -> return "lazagna_sheets"
            "докторская колбаса" -> return "doctor_sausage"
            "курица", "куриная грудка", "куриное филе", "филе курицы", "грудка курицы" -> return "chicken"
            "карри", "соус карри", "кари", "соус карри" -> return "kari"
            "макароны", "спагетти" -> return "pasta_package"
            "бульон", "мясной бульон", "куриный бульон", "говяжий бульон", "свиной бульон" -> return "bouillon"
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

    fun invertColors(imageView: ImageView?) {
        val matrix = ColorMatrix().apply {
            set(floatArrayOf(
                -1f,  0f,  0f, 0f, 255f,
                0f, -1f,  0f, 0f, 255f,
                0f,  0f, -1f, 0f, 255f,
                0f,  0f,  0f, 1f, 0f
            ))
        }
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.setColorFilter(filter)
    }

    fun makeImageWhite(imageView: ImageView?) {
        val matrix = ColorMatrix().apply {
            set(floatArrayOf(
                1f, 0f, 0f, 0f, 255f, // Увеличение красного канала
                0f, 1f, 0f, 0f, 255f, // Увеличение зеленого канала
                0f, 0f, 1f, 0f, 255f, // Увеличение синего канала
                0f, 0f, 0f, 1f, 0f    // Не изменять альфа-канал
            ))
        }
        val filter = ColorMatrixColorFilter(matrix)
        imageView?.colorFilter = filter
    }

}