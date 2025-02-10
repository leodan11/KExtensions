@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.context

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.github.leodan11.k_extensions.core.tag
import com.google.android.material.textfield.TextInputLayout

/**
 * Get application cache directory
 *
 * Application cache directory ("/data/data/<package name>/cache")
 *
 * @return [String] - Cache directory
 *
 */
val Context.cacheDirPath: String
    get() = cacheDir.absolutePath


/**
 * Get Connectivity manager
 */
val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


/**
 * Get the application external cache directory
 *
 * Application cache directory ("/Android/data/<package name>/cache")
 *
 * @return [String] - External cache directory
 *
 */
val Context.externalCacheDirPath: String
    get() = externalCacheDir?.absolutePath ?: ""


/**
 * Get the application external file directory
 *
 * Application file directory ("/Android/data/<package name>/files")
 *
 * @return [String] - External file directory
 *
 */
val Context.externalFileDirPath: String
    get() = getExternalFilesDir("")?.absolutePath ?: ""


/**
 * Get application file directory
 *
 * Application file directory ("/data/data/<package name>/files")
 *
 * @return [String] - File directory
 *
 */
val Context.fileDirPath: String
    get() = filesDir.absolutePath


/**
 * Get Window manager
 */
val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager


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
 * Get color from resources
 */
fun Context.compatColor(@ColorRes colorInt: Int): Int =
    ContextCompat.getColor(this, colorInt)


/**
 * Get drawable from resources
 */
fun Context.compatDrawable(@DrawableRes drawableRes: Int): Drawable? =
    try {
        ContextCompat.getDrawable(this, drawableRes)
    } catch (e: Exception) {
        AppCompatResources.getDrawable(this, drawableRes)
    }


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
fun Context.createBitmap(
    width: Float,
    height: Float,
    backgroundColor: Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap {
    Log.i(this.tag(), "createBitmap")
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
 * Extension method to provide quicker access to the [LayoutInflater] from [Context].
 */
fun Context.getLayoutInflater() =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


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
 * Check if a service is running
 *
 * @return [Boolean] - `true` or `false`
 */
inline fun <reified T : Service> Context.isServiceRunning(): Boolean {
    (this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?)?.run {
        for (service in getRunningServices(Integer.MAX_VALUE)) {
            if (T::class.java.name == service.service.className) return true //service.foreground
        }
    }
    return false
}


/**
 * Reboot the application
 *
 * @param[restartIntent] optional, desired activity to show after the reboot
 */
fun Context.reboot(restartIntent: Intent? = this.packageManager.getLaunchIntentForPackage(this.packageName)) {
    restartIntent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (this is Activity) {
        this.startActivity(restartIntent)
        finishAffinity()
    } else {
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        this.startActivity(restartIntent)
    }
}


/**
 * Register a receiver to listen to bluetooth changes
 *
 * @param connectionStateChanged [(Intent, Int) -> Unit] - Bluetooth connection state changed callback
 * @return [BroadcastReceiver] - Broadcast receiver
 */
inline fun Context.registerBluetoothChange(crossinline connectionStateChanged: (Intent, Int) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
                connectionStateChanged(intent, intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1))
            }
        }
    }.apply {
        val intent =
            IntentFilter().apply { addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) }
        this@registerBluetoothChange.registerReceiver(this, intent)
    }
}


/**
 * Register a receiver to listen to volume changes
 *
 * @param block [Int] - Volume changed callback
 * @return [BroadcastReceiver] - Broadcast receiver
 */
inline fun Context.registerVolumeChange(crossinline block: (Int) -> Unit): BroadcastReceiver {
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == "android.media.VOLUME_CHANGED_ACTION") {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                block(currVolume)
            }
        }
    }.apply {
        val intent = IntentFilter().apply { addAction("android.media.VOLUME_CHANGED_ACTION") }
        this@registerVolumeChange.registerReceiver(this, intent)
    }
}


/**
 * Register a receiver to listen to wifi state changes
 *
 * @param callback [Intent] - Wifi state changed callback
 * @return [BroadcastReceiver] - Broadcast receiver
 */
inline fun Context.registerWifiStateChanged(crossinline callback: (Intent) -> Unit): BroadcastReceiver {
    val action = "android.net.wifi.WIFI_STATE_CHANGED"
    return object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == action) {
                callback(intent)
            }
        }
    }.apply {
        val intent = IntentFilter().apply { addAction(action) }
        this@registerWifiStateChanged.registerReceiver(this, intent)
    }
}


/**
 * Start a foreground service
 *
 * @param predicate [Intent] - Predicate to set the service intent
 */
inline fun <reified T : Service> Context.startForegroundService(predicate: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    predicate(intent)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}


/**
 * Start a service unless it is already running
 *
 * @param predicate [Intent] - Predicate to set the service intent
 */
inline fun <reified T : Service> Context.startServiceUnlessRunning(predicate: Intent.() -> Unit = {}) {
    if (!this.isServiceRunning<T>()) this.startForegroundService<T>(predicate)
}


/**
 * Stop a service
 *
 * @return [Boolean] - `true` or `false`
 */
inline fun <reified T : Service> Context.stopService(): Boolean {
    val intent = Intent(this, T::class.java)
    return stopService(intent)
}


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
 * Validate Text field has data. Default error message [R.string.label_this_field_cannot_be_left_empty]
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 *
 * @return [Boolean] `true` or `false`.
 *
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText
): Boolean {
    val errorDefault = this.getString(R.string.label_this_field_cannot_be_left_empty)
    return this.validateTextField(inputLayout, inputEditText, errorDefault)
}


/**
 * Validate Text field has data
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 * @param message [String] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    message: String
): Boolean {
    Log.i(this.tag(), "validateTextField")
    inputEditText.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = message
            return false
        } else inputLayout.isErrorEnabled = false
        return true
    }
}


/**
 * Validate Text field has data. Default error message [R.string.label_this_field_cannot_be_left_empty]
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView
): Boolean {
    val errorDefault = this.getString(R.string.label_this_field_cannot_be_left_empty)
    return this.validateTextField(inputLayout, inputAutoComplete, errorDefault)
}


/**
 * Validate Text field has data
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 * @param message [String] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    message: String
): Boolean {
    Log.i(this.tag(), "validateTextField")
    inputAutoComplete.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = message
            return false
        } else inputLayout.isErrorEnabled = false
        return true
    }
}


/**
 * Validate Text field has data
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 * @param message [Int] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    @StringRes message: Int
): Boolean {
    inputEditText.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = this.getString(message)
            return false
        } else inputLayout.isErrorEnabled = false
        return true
    }
}


/**
 * Validate Text field has data
 *
 * @receiver [Context] required current context
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 * @param message [Int] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    @StringRes message: Int
): Boolean {
    inputAutoComplete.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = this.getString(message)
            return false
        } else inputLayout.isErrorEnabled = false
        return true
    }
}