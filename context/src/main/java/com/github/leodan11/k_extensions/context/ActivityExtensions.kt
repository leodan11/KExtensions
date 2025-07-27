@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.context

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
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.github.leodan11.k_extensions.core.DisplayDensity
import androidx.core.graphics.createBitmap
import androidx.fragment.app.FragmentActivity

/**
 * Registers a custom [OnBackPressedCallback] for this [FragmentActivity].
 *
 * This allows handling the system back button with custom behavior in activities.
 *
 * @receiver FragmentActivity where the back press callback is registered.
 * @param callback The [OnBackPressedCallback] instance that defines the back press logic.
 *
 * ```kotlin
 * /** MainActivity::class */
 * addOnBackPressedCallback(object : OnBackPressedCallback(true) {
 *        override fun handleOnBackPressed() {
 *            // Handle back press in activity
 *       }
 * })
 * ```
 */
fun FragmentActivity.addOnBackPressedCallback(callback: OnBackPressedCallback) {
    onBackPressedDispatcher.addCallback(this, callback)
}

/**
 * Enables fullscreen mode for the Activity.
 *
 * @receiver Activity on which fullscreen mode will be enabled.
 *
 */
fun Activity.enableFullscreen() {
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

/**
 * Enables immersive mode, hiding system UI and making content appear under system bars.
 *
 * @receiver Activity on which immersive mode will be enabled.
 *
 */
fun Activity.enableImmersiveMode() {
    val window = window
    window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
    window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
        if (visibility != 0) return@setOnSystemUiVisibilityChangeListener
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

/**
 * Enters fullscreen mode by clearing force-not-fullscreen flag and adding fullscreen flag.
 *
 * @receiver Activity to enter fullscreen mode.
 */
@UiThread
fun Activity.enterFullscreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

/**
 * Exits fullscreen mode by clearing fullscreen flag and adding force-not-fullscreen flag.
 *
 * @receiver Activity to exit fullscreen mode.
 */
@UiThread
fun Activity.exitFullscreenMode() {
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
}

/**
 * Hides the soft keyboard if any view in the Activity currently has focus.
 *
 * @receiver Activity where keyboard will be hidden.
 *
 */
fun Activity.hideSoftKeyboard() {
    val view: View? = currentFocus
    view?.clearFocus()
    if (view != null) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * Shows system UI by removing all flags except those that allow content to appear under system bars.
 *
 * @receiver Activity where system UI will be shown.
 *
 */
fun Activity.showSystemUI() {
    window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            )
}

/**
 * Updates system UI visibility when window focus changes to show system bars.
 *
 * @receiver Activity reacting to window focus change.
 */
fun Activity.onWindowFocusChanged() {
    if (hasWindowFocus()) {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }
}

/**
 * Hides system UI (status and navigation bars) to enable immersive mode.
 *
 * @receiver Activity where system UI will be hidden.
 *
 */
fun Activity.hideSystemUI() {
    window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
            )
}

/**
 * Takes a screenshot of the current activity's window.
 *
 * @receiver Activity where the screenshot will be taken.
 * @param removeStatusBar Whether to remove the status bar area from the screenshot.
 * @return Bitmap of the screenshot.
 *
 */
fun Activity.takeScreenshot(removeStatusBar: Boolean = false): Bitmap {
    val dm = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(dm)

    val bitmap = createBitmap(dm.widthPixels, dm.heightPixels, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    window.decorView.draw(canvas)

    return if (removeStatusBar) {
        val statusBarHeight = statusBarHeight
        Bitmap.createBitmap(
            bitmap,
            0,
            statusBarHeight,
            dm.widthPixels,
            dm.heightPixels - statusBarHeight
        )
    } else {
        Bitmap.createBitmap(bitmap, 0, 0, dm.widthPixels, dm.heightPixels)
    }
}

/**
 * Takes a screenshot asynchronously using PixelCopy.
 *
 * @receiver Activity where the screenshot will be taken.
 * @param removeStatusBar Whether to remove the status bar area from the screenshot.
 * @param listener Callback invoked with the result code and the bitmap.
 *
 * ```kotlin
 * takeScreenshot(removeStatusBar = true) { resultCode, bitmap ->
 *     if (resultCode == PixelCopy.SUCCESS) {
 *         // Handle the bitmap
 *     }
 * }
 * ```
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Activity.takeScreenshot(
    removeStatusBar: Boolean = false,
    listener: (resultCode: Int, bitmap: Bitmap) -> Unit
) {
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

/**
 * Restarts the current Activity with an optional intent builder to add extras or flags.
 * The existing extras are preserved.
 *
 * @receiver Activity to be restarted.
 * @param intentBuilder Lambda to configure the Intent before restarting.
 *
 */
inline fun Activity.restart(intentBuilder: Intent.() -> Unit = {}) {
    val intent = Intent(this, this::class.java)
    intent.extras?.let { intent.putExtras(it) }
    intent.intentBuilder()
    startActivity(intent)
    finish()
}

/**
 * Creates an ActivityOptionsCompat for scene transition animations between Activities.
 *
 * @receiver Activity starting the transition.
 * @param view The View to be transitioned.
 * @param transitionName The name of the transition.
 * @return ActivityOptionsCompat configured for the transition.
 *
 * ```kotlin
 * val options = makeSceneTransitionAnimation(imageView, "profileImage")
 * startActivity(intent, options.toBundle())
 * ```
 */
fun Activity.makeSceneTransitionAnimation(
    view: View,
    transitionName: String
): ActivityOptionsCompat =
    ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, transitionName)

/**
 * Starts a new Activity.
 *
 * @receiver Activity starting the new page.
 * @param page Class of the target Activity.
 * @param finishCurrent Whether to finish the current Activity (default true).
 * @param block Lambda to configure the Intent before starting the Activity.
 *
 * ```kotlin
 * startNewPage(DetailActivity::class.java) {
 *     putExtra("id", 123)
 * }
 * ```
 */
fun Activity.startNewPage(
    page: Class<*>,
    finishCurrent: Boolean = true,
    block: Intent.() -> Unit = {}
) {
    val intent = Intent(this, page).apply(block)
    startActivity(intent)
    if (finishCurrent) finish()
}

/**
 * Gets the display density category as [DisplayDensity].
 *
 * @receiver AppCompatActivity to get the display density from.
 * @return The display density enum.
 *
 * ```kotlin
 * val density = getDisplayDensity()
 * ```
 */
fun AppCompatActivity.getDisplayDensity(): DisplayDensity {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
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
 * Gets the status bar height in pixels.
 *
 * @receiver Activity to get the status bar height.
 * @return Status bar height in pixels.
 */
private val Activity.statusBarHeight: Int
    @SuppressLint("InternalInsetResource", "DiscouragedApi")
    get() {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }
