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
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.net.ConnectivityManager
import android.os.Build
import android.text.TextUtils
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
import androidx.core.graphics.drawable.toDrawable
import com.google.android.material.textfield.TextInputLayout

/**
 * Returns the absolute path of the application cache directory.
 *
 * @receiver Context - The context of the current state of the application.
 * @return String - Absolute path of cache directory.
 */
val Context.cacheDirPath: String
    get() = cacheDir.absolutePath

/**
 * Returns the ConnectivityManager system service.
 *
 * @receiver Context
 * @return ConnectivityManager - The connectivity manager.
 */
val Context.connectivityManager: ConnectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

/**
 * Returns the absolute path of the external cache directory.
 *
 * @receiver Context
 * @return String - External cache directory path or empty string if null.
 */
val Context.externalCacheDirPath: String
    get() = externalCacheDir?.absolutePath ?: ""

/**
 * Returns the absolute path of the external files directory.
 *
 * @receiver Context
 * @return String - External file directory path or empty string if null.
 */
val Context.externalFileDirPath: String
    get() = getExternalFilesDir("")?.absolutePath ?: ""

/**
 * Returns the absolute path of the internal files directory.
 *
 * @receiver Context
 * @return String - Internal files directory path.
 */
val Context.fileDirPath: String
    get() = filesDir.absolutePath

/**
 * Returns the WindowManager system service.
 *
 * @receiver Context
 * @return WindowManager - The window manager.
 */
val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager


/**
 * Converts dp (density-independent pixels) to pixels.
 *
 * @receiver Context
 * @param dp Float - Value in dp to convert.
 * @return Float - Converted value in pixels.
 */
fun Context.convertDpToPx(dp: Float): Float = resources.displayMetrics.density * dp

/**
 * Converts pixels to dp (density-independent pixels).
 *
 * @receiver Context
 * @param px Float - Value in pixels to convert.
 * @return Float - Converted value in dp.
 */
fun Context.convertPxToDp(px: Float): Float = px / resources.displayMetrics.density

/**
 * Creates a bitmap of a custom shape with the specified width, height, and background color.
 *
 * @receiver Context
 * @param width Float - Width of the bitmap.
 * @param height Float - Height of the bitmap.
 * @param backgroundColor Int - Color to fill the bitmap.
 * @param config Bitmap.Config - Bitmap configuration (default ARGB_8888).
 * @return Bitmap - The generated bitmap.
 * @throws IllegalArgumentException If width or height is invalid.
 */
fun Context.createBitmap(
    width: Float,
    height: Float,
    backgroundColor: Int,
    config: Bitmap.Config = Bitmap.Config.ARGB_8888
): Bitmap {
    val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), config)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = backgroundColor
    }
    val path = Path().apply {
        moveTo(0f, 0f)
        lineTo(width, 0f)
        lineTo(width / 2, height)
        close()
    }
    canvas.drawPath(path, paint)
    return bitmap
}

/**
 * Retrieves a color from resources in a backward compatible way.
 *
 * @receiver Context
 * @param colorResId Int - Resource ID of the color.
 * @return Int - The resolved color integer.
 */
fun Context.compatColor(@ColorRes colorResId: Int): Int = ContextCompat.getColor(this, colorResId)

/**
 * Retrieves a drawable from resources in a backward compatible way.
 *
 * @receiver Context
 * @param drawableResId Int - Resource ID of the drawable.
 * @return Drawable? - The resolved drawable or null if not found.
 */
fun Context.compatDrawable(@DrawableRes drawableResId: Int): Drawable? = try {
    ContextCompat.getDrawable(this, drawableResId)
} catch (e: Exception) {
    AppCompatResources.getDrawable(this, drawableResId)
}

/**
 * Retrieves the resource ID associated with a given attribute resource.
 *
 * @receiver Context
 * @param idAttrRes Int - Attribute resource ID (e.g., android.R.attr.colorAccent).
 * @return Int - The resolved resource ID.
 */
fun Context.customResolverResourceId(@AttrRes idAttrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(idAttrRes, typedValue, true)
    return typedValue.resourceId
}

/**
 * Returns a drawable that contains the specified text.
 *
 * @receiver Context
 * @param text String - The text to display.
 * @param typeface Typeface? - Optional typeface (default is bold default).
 * @param color Int - Color resource ID of the text color.
 * @param size Int - Text size in sp.
 * @return Drawable - Drawable containing the text.
 */
fun Context.getDrawableText(
    text: String,
    typeface: Typeface? = null,
    @ColorRes color: Int,
    size: Int
): Drawable {
    val bitmap = Bitmap.createBitmap(48, 48, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val scale = resources.displayMetrics.density
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
    return bitmap.toDrawable(resources)
}

/**
 * Returns the version code of the application package.
 *
 * @receiver Context
 * @param pkgName String - Package name (defaults to current package).
 * @return Long - Version code or -1 if not found.
 */
fun Context.getVersionCode(pkgName: String = packageName): Long {
    if (pkgName.isBlank()) return -1
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(pkgName, 0).longVersionCode
        } else {
            packageManager.getPackageInfo(pkgName, 0).versionCode.toLong()
        }
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }
}

/**
 * Returns the version name of the application package.
 *
 * @receiver Context
 * @param pkgName String - Package name (defaults to current package).
 * @return String - Version name or empty string if not found.
 */
fun Context.getVersionName(pkgName: String = packageName): String {
    if (pkgName.isBlank()) return ""
    return try {
        packageManager.getPackageInfo(pkgName, 0).versionName ?: ""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

/**
 * Provides quick access to the LayoutInflater from the Context.
 *
 * @receiver Context
 * @return LayoutInflater - The layout inflater.
 */
fun Context.onLayoutInflater(): LayoutInflater =
    getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

/**
 * Returns whether the device is currently in night mode.
 *
 * @receiver Context
 * @return Boolean - True if night mode is active, false otherwise.
 */
fun Context.isNightModeActive(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        resources.configuration.isNightModeActive
    } else {
        val darkModeFlag = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }
}

/**
 * Checks if a service of type T is currently running.
 *
 * @receiver Context
 * @return Boolean - True if service is running, false otherwise.
 */
inline fun <reified T : Service> Context.isServiceRunning(): Boolean {
    val activityManager =
        getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return false
    for (service in activityManager.getRunningServices(Int.MAX_VALUE)) {
        if (service.service.className == T::class.java.name) return true
    }
    return false
}

/**
 * Reboots the application by restarting the specified activity or the main launcher activity by default.
 *
 * @receiver Context
 * @param restartIntent Intent? - Optional Intent to launch after reboot.
 */
fun Context.reboot(restartIntent: Intent? = packageManager.getLaunchIntentForPackage(packageName)) {
    restartIntent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    if (this is Activity) {
        startActivity(restartIntent)
        finishAffinity()
    } else {
        restartIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(restartIntent)
    }
}

/**
 * Registers a BroadcastReceiver to listen for Bluetooth connection state changes.
 *
 * @receiver Context
 * @param connectionStateChanged (Intent, Int) -> Unit - Callback with Intent and connection state.
 * @return BroadcastReceiver - The registered receiver.
 */
inline fun Context.registerBluetoothChange(crossinline connectionStateChanged: (Intent, Int) -> Unit): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED) {
                val state = intent.getIntExtra(BluetoothProfile.EXTRA_STATE, -1)
                connectionStateChanged(intent, state)
            }
        }
    }
    val filter = IntentFilter(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
    registerReceiver(receiver, filter)
    return receiver
}

/**
 * Registers a BroadcastReceiver to listen for volume changes.
 *
 * @receiver Context
 * @param block (Int) -> Unit - Callback with the current volume level.
 * @return BroadcastReceiver - The registered receiver.
 */
inline fun Context.registerVolumeChange(crossinline block: (Int) -> Unit): BroadcastReceiver {
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action ?: return
            if (action == "android.media.VOLUME_CHANGED_ACTION") {
                val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
                val currVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                block(currVolume)
            }
        }
    }
    val filter = IntentFilter("android.media.VOLUME_CHANGED_ACTION")
    registerReceiver(receiver, filter)
    return receiver
}

/**
 * Registers a BroadcastReceiver to listen for WiFi state changes.
 *
 * @receiver Context
 * @param callback (Intent) -> Unit - Callback when WiFi state changes.
 * @return BroadcastReceiver - The registered receiver.
 */
inline fun Context.registerWifiStateChanged(crossinline callback: (Intent) -> Unit): BroadcastReceiver {
    val action = "android.net.wifi.WIFI_STATE_CHANGED"
    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == action) {
                callback(intent)
            }
        }
    }
    val filter = IntentFilter(action)
    registerReceiver(receiver, filter)
    return receiver
}

/**
 * Starts a foreground service of type T with optional intent customization.
 *
 * @receiver Context
 * @param predicate Intent.() -> Unit - Lambda to customize the intent.
 */
inline fun <reified T : Service> Context.startForegroundService(predicate: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java).apply(predicate)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

/**
 * Starts a service of type T only if it is not already running.
 *
 * @receiver Context
 * @param predicate Intent.() -> Unit - Lambda to customize the intent.
 */
inline fun <reified T : Service> Context.startServiceUnlessRunning(predicate: Intent.() -> Unit = {}) {
    if (!isServiceRunning<T>()) startForegroundService<T>(predicate)
}

/**
 * Stops a service of type T.
 *
 * @receiver Context
 * @return Boolean - True if service was stopped, false otherwise.
 */
inline fun <reified T : Service> Context.stopService(): Boolean {
    val intent = Intent(this, T::class.java)
    return stopService(intent)
}

/**
 * Converts a drawable resource to a Bitmap.
 *
 * @receiver Context
 * @param drawableIdRes Int - Drawable resource ID.
 * @return Bitmap - The bitmap representation.
 * @throws Exception if drawable resource is invalid.
 */
fun Context.toDrawableAsBitmap(@DrawableRes drawableIdRes: Int): Bitmap {
    val drawable = ContextCompat.getDrawable(this, drawableIdRes)
        ?: throw Exception("Invalid drawable resource")
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}

/**
 * Validates that a TextInputLayout's EditText has non-empty input.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout of the EditText.
 * @param inputEditText EditText - The text input field.
 * @return Boolean - True if input is valid, false otherwise.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText
): Boolean {
    val errorDefault =
        getString(com.github.leodan11.k_extensions.core.R.string.label_this_field_cannot_be_left_empty)
    return validateTextField(inputLayout, inputEditText, errorDefault)
}

/**
 * Validates that a TextInputLayout's EditText has non-empty input, with custom error message string.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout.
 * @param inputEditText EditText - Text field.
 * @param message String - Custom error message.
 * @return Boolean - True if input is valid.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    message: String
): Boolean {
    inputEditText.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = message
            return false
        }
        inputLayout.isErrorEnabled = false
        return true
    }
}

/**
 * Validates that a TextInputLayout's EditText has non-empty input, with custom error message resource.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout.
 * @param inputEditText EditText - Text field.
 * @param message Int - String resource for the error message.
 * @return Boolean - True if input is valid.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    @StringRes message: Int
): Boolean {
    return validateTextField(inputLayout, inputEditText, getString(message))
}

/**
 * Validates that a TextInputLayout's AutoCompleteTextView has non-empty input.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout.
 * @param inputAutoComplete AutoCompleteTextView - The input field.
 * @return Boolean - True if input is valid.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView
): Boolean {
    val errorDefault =
        getString(com.github.leodan11.k_extensions.core.R.string.label_this_field_cannot_be_left_empty)
    return validateTextField(inputLayout, inputAutoComplete, errorDefault)
}

/**
 * Validates that a TextInputLayout's AutoCompleteTextView has non-empty input, with custom error message string.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout.
 * @param inputAutoComplete AutoCompleteTextView - Input field.
 * @param message String - Custom error message.
 * @return Boolean - True if input is valid.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    message: String
): Boolean {
    inputAutoComplete.let {
        if (TextUtils.isEmpty(it.text.toString().trimEnd())) {
            inputLayout.isErrorEnabled = true
            inputLayout.error = message
            return false
        }
        inputLayout.isErrorEnabled = false
        return true
    }
}

/**
 * Validates that a TextInputLayout's AutoCompleteTextView has non-empty input, with custom error message resource.
 *
 * @receiver Context
 * @param inputLayout TextInputLayout - Parent layout.
 * @param inputAutoComplete AutoCompleteTextView - Input field.
 * @param message Int - String resource for the error message.
 * @return Boolean - True if input is valid.
 */
fun Context.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    @StringRes message: Int
): Boolean {
    return validateTextField(inputLayout, inputAutoComplete, getString(message))
}