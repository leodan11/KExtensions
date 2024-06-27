@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.core

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.os.Build
import android.util.TypedValue
import android.view.WindowManager
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat

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
 * Get the application version code
 *
 * @param pkgName [String] - Package name
 * @return [Long] - Version code
 *
 */
fun Context.getVersionCode(pkgName: String = packageName): Long {
    if (pkgName.isBlank()) return -1
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageManager.getPackageInfo(pkgName, 0).longVersionCode
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