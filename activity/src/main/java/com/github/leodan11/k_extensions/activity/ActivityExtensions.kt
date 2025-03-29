@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.util.DisplayMetrics
import android.view.PixelCopy
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.github.leodan11.k_extensions.core.DisplayDensity
import androidx.core.graphics.createBitmap

/**
 * Enable fullscreen mode
 */
fun Activity.enableFullScreen() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}


/**
 * Enable immersive mode
 */
fun Activity.enableImmersiveMode() {
    val window = window
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
        if (visibility != 0)
            return@setOnSystemUiVisibilityChangeListener
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}


/**
 *  Makes the activity enter fullscreen mode.
 */
@UiThread
fun Activity.enterFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}


/**
 * Makes the Activity exit fullscreen mode.
 */
@UiThread
fun Activity.exitFullScreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
}


/**
 * This snippet hides the system bars.
 */
fun Activity.hideSystemUI() {
    // Set the IMMERSIVE flag.
    // Set the content to appear under the system bars so that the content
    // doesn't resize when the system bars hide and show.
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar

            or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar

            or View.SYSTEM_UI_FLAG_IMMERSIVE)
}


/**
 * Hide input keyboard view
 *
 */
fun Activity.onHideSoftKeyboard() {
    val view: View? = this.currentFocus
    view?.clearFocus()
    if (view != null) {
        val input = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(view.windowToken, 0)
    }
}


/**
 * This snippet shows the system bars. It does this by removing all the flags
 */
fun Activity.onWindowFocusChanged() {
    if (this.hasWindowFocus()) {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }
}


/**
 * This snippet shows the system bars. It does this by removing all the flags
 * except for the ones that make the content appear under the system bars.
 */
fun Activity.showSystemUI() {
    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
}


/**
 * Returns display density as ...DPI
 *
 * @return [DisplayDensity]
 */
fun AppCompatActivity.getDisplayDensity(): DisplayDensity {
    val metrics = DisplayMetrics()
    this.windowManager.defaultDisplay.getMetrics(metrics)
    return when (metrics.densityDpi) {
        DisplayMetrics.DENSITY_LOW -> DisplayDensity.LDPI
        DisplayMetrics.DENSITY_MEDIUM -> DisplayDensity.MDPI
        DisplayMetrics.DENSITY_HIGH -> DisplayDensity.HDPI
        DisplayMetrics.DENSITY_XHIGH -> DisplayDensity.XHDPI
        DisplayMetrics.DENSITY_XXHIGH -> DisplayDensity.XXHDPI
        DisplayMetrics.DENSITY_XXXHIGH -> DisplayDensity.XXXHDPI
        else -> DisplayDensity.XXHDPI
    }
}

/**
 * Restarts an activity from itself with a fade animation
 * Keeps its existing extra bundles and has a intentBuilder to accept other parameters
 *
 * @param intentBuilder [Intent] - Intent builder
 *
 */
inline fun Activity.restart(intentBuilder: Intent.() -> Unit = {}) {
    val i = Intent(this, this::class.java)
    val oldExtras = intent.extras
    if (oldExtras != null)
        i.putExtras(oldExtras)
    i.intentBuilder()
    startActivity(i)
    finish()
}


/**
 * Create an ActivityOptions to transition between Activities using cross-Activity scene animations.
 *
 * @param view [View] - View to transition
 * @param transitionName [String] - Transition name
 * @return [ActivityOptionsCompat]
 */
fun Activity.makeSceneTransitionAnimation(
    view: View,
    transitionName: String
): ActivityOptionsCompat =
    ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName)


/**
 * Screen shot
 *
 * @param removeStatusBar [Boolean] - Remove status bar
 * @return [Bitmap]
 *
 */
fun Activity.screenShot(removeStatusBar: Boolean = false): Bitmap {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)

    val bmp = createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.RGB_565)
    val canvas = Canvas(bmp)
    window.decorView.draw(canvas)


    return if (removeStatusBar) {
        val statusBarHeight = statusBarHeight
        Bitmap.createBitmap(
            bmp,
            0,
            statusBarHeight,
            dm.widthPixels,
            dm.heightPixels - statusBarHeight
        )
    } else {
        Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels)
    }
}


/**
 * Screen shot
 *
 * @param removeStatusBar [Boolean] - Remove status bar
 * @param listener ([Int], [Bitmap]) - Listener
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.screenShot(removeStatusBar: Boolean = false, listener: (Int, Bitmap) -> Unit) {

    val rect = Rect()
    windowManager.defaultDisplay.getRectSize(rect)

    if (removeStatusBar) {
        val statusBarHeight = statusBarHeight

        rect.set(rect.left, rect.top + statusBarHeight, rect.right, rect.bottom)
    }
    val bitmap = createBitmap(rect.width(), rect.height())

    PixelCopy.request(this.window, rect, bitmap, {
        listener(it, bitmap)
    }, Handler(this.mainLooper))
}



private val Activity.statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(id)
    }

