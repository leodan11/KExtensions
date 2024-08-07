@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.util.TypedValue
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout

/**
 * Get Connectivity manager
 */
val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


/**
 * Convert dp to px
 *
 * @param dp
 * @return [Float]
 */
fun Context.convertDpToPx(dp: Float): Float = (this.resources.displayMetrics.density * dp)


/**
 * Convert px to dp
 *
 * @param px
 * @return [Float]
 */
fun Context.convertPxToDp(px: Float): Float = (px / this.resources.displayMetrics.density)

/**
 * Creates a bitmap from a specific color and size.
 *
 * @param width Defines the width of the bitmap
 * @param height Defines the height of the bitmap
 * @param backgroundColor Defines the color of the bitmap
 * @param config [Bitmap.Config] Default [Bitmap.Config.ARGB_8888]
 *
 * @return [Bitmap]
 *
 * @throws IllegalArgumentException
 *
 */
fun Context.createBitmap(width: Float, height: Float, backgroundColor: Int, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap {
    val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), config)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    paint.setColor(backgroundColor)
    val path = Path()
    path.moveTo(0f, 0f)
    path.lineTo(width, 0f)
    path.lineTo(width / 2, height)
    path.close()
    canvas.drawPath(path, paint)
    return bitmap
}


/**
 * Get resource id
 *
 * @param idAttrRes ID attr Resource, e.g: [android.R.attr.colorAccent]
 * @return [Int] - Resource Id
 */
fun Context.customResolverResourceId(@AttrRes idAttrRes: Int): Int {
    val typedValue = TypedValue()
    this.theme.resolveAttribute(idAttrRes, typedValue, true)
    return typedValue.resourceId
}

/**
 * Utils method to create drawable containing text
 * @param text [String] - Text
 * @param typeface [Typeface] - Typeface
 * @param color [Int] - Color
 * @param size [Int] - Size
 * @return [Drawable]
 */
fun Context.getDrawableText(
    text: String,
    typeface: Typeface? = null,
    color: Int,
    size: Int
): Drawable {
    val bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(bitmap)
    val scale = this.resources.displayMetrics.density

    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.typeface = typeface ?: Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        this.color = ContextCompat.getColor(this@getDrawableText, color)
        this.textSize = (size * scale).toInt().toFloat()
    }

    val bounds = Rect()
    paint.getTextBounds(text, 0, text.length, bounds)
    val x = (bitmap.width - bounds.width()) / 2
    val y = (bitmap.height + bounds.height()) / 2
    canvas.drawText(text, x.toFloat(), y.toFloat(), paint)

    return BitmapDrawable(this.resources, bitmap)
}


/**
 * Get the application version code
 *
 * @param pkgName [String] - Package name
 * @return [Long] - Version code
 *
 */
fun Context.getVersionCode(pkgName: String = packageName): Long {
    if (pkgName.isBlank()) return -1
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageManager.getPackageInfo(
            pkgName,
            0
        ).longVersionCode
        else packageManager.getPackageInfo(pkgName, 0).versionCode.toLong()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}

/**
 * Get the application version name
 *
 * @param pkgName [String] - Package name
 * @return [String] - Version name
 */
fun Context.getVersionName(pkgName: String = packageName): String {
    if (pkgName.isBlank()) return ""
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}


/**
 * Determine if dark mode is currently active
 *
 * @return [Boolean] - It is active
 */
fun Context.isNightModeActive(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.resources.configuration.isNightModeActive
    } else {
        val darkModeFlag = this.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}


/**
 * Get Window manager
 */
val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager


/**
 * Get bitmap from drawable
 *
 * @param drawableIdRes ID drawable resource
 * @return [Bitmap]
 */
fun Context.toDrawableAsBitmap(@DrawableRes drawableIdRes: Int): Bitmap {
    val drawable =
        ContextCompat.getDrawable(this, drawableIdRes) ?: throw Exception("Invalid drawable")
    val canvas = Canvas()
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    canvas.setBitmap(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}


/**
 * Get application file directory
 *
 * Application file directory ("/data/data/<package name>/files")
 */
val Context.fileDirPath: String
    get() = filesDir.absolutePath

/**
 * Get application cache directory
 *
 * Application cache directory ("/data/data/<package name>/cache")
 */
val Context.cacheDirPath: String
    get() = cacheDir.absolutePath

/**
 * Get the application external file directory
 *
 * Application file directory ("/Android/data/<package name>/files")
 */
val Context.externalFileDirPath: String
    get() = getExternalFilesDir("")?.absolutePath ?: ""

/**
 * Get the application external cache directory
 *
 * Application cache directory ("/Android/data/<package name>/cache")
 */
val Context.externalCacheDirPath: String
    get() = externalCacheDir?.absolutePath ?: ""


/**
 * Validate Text field has data
 *
 * @param textInputLayout Parent element [TextInputLayout].
 * @param textInputEditText Text field [EditText].
 * @param errorMessage [String] Error to be displayed on the element. Default NULL
 * @param errorResource [Int] Error to be displayed on the element. Default NULL
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    textInputLayout: TextInputLayout,
    textInputEditText: EditText,
    errorMessage: String? = null,
    @StringRes errorResource: Int? = null,
): Boolean {
    if (TextUtils.isEmpty(textInputEditText.text.toString().trim())) {
        textInputLayout.isErrorEnabled = true
        if (errorMessage.isNullOrEmpty()){
            if (errorResource != null) {
                textInputLayout.error = this.getString(errorResource)
            }
        } else textInputLayout.error = errorMessage
        return false
    } else textInputLayout.isErrorEnabled = false
    return true
}