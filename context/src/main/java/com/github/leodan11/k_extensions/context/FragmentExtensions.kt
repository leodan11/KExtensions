package com.github.leodan11.k_extensions.context

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.AttrRes
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.github.leodan11.k_extensions.core.UnitType
import com.github.leodan11.k_extensions.core.getDisplayText
import com.github.leodan11.k_extensions.core.internetOn
import com.github.leodan11.k_extensions.core.toElapsedTimeString
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


/**
 * Adds a [MenuProvider] to the [Fragment]'s parent [MenuHost] (usually the hosting [Activity]),
 * scoped to the [Fragment]'s `viewLifecycleOwner` and a specified minimum [Lifecycle.State].
 *
 * This extension simplifies integrating Jetpack's `MenuProvider` API inside a [Fragment],
 * ensuring the menu is only available when the [Fragment]'s view lifecycle is at least in the specified state.
 *
 * ```kotlin
 * //example
 * override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
 *     addMenuProvider(object : MenuProvider {
 *         override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
 *             menuInflater.inflate(R.menu.sample_menu, menu)
 *         }
 *
 *         override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
 *             return when (menuItem.itemId) {
 *                 R.id.action_settings -> {
 *                     openSettings()
 *                     true
 *                 }
 *                 else -> false
 *             }
 *         }
 *     })
 * }
 * ```
 *
 * @receiver The [Fragment] to which the menu provider is being added.
 * @param menuProvider The [MenuProvider] responsible for populating and handling the menu.
 * @param lifecycleState The minimum lifecycle state required for the menu to be visible. Defaults to [Lifecycle.State.RESUMED].
 *
 * @see androidx.core.view.MenuProvider
 * @see androidx.core.view.MenuHost
 */
@MainThread
fun Fragment.addMenuProvider(menuProvider: MenuProvider, lifecycleState: Lifecycle.State = Lifecycle.State.RESUMED) {
    val menuHost: MenuHost = requireActivity()
    menuHost.addMenuProvider(menuProvider, viewLifecycleOwner, lifecycleState)
}


/**
 * Gets the absolute path to the app's internal cache directory.
 *
 * Example path: `/data/data/<package_name>/cache`
 *
 * @receiver Fragment instance.
 * @return Absolute cache directory path as [String].
 *
 * ```kotlin
 * val cachePath = fragment.cacheDirPath
 * ```
 */
val Fragment.cacheDirPath: String
    get() = requireActivity().cacheDir.absolutePath

/**
 * Gets the absolute path to the app's internal files directory.
 *
 * Example path: `/data/data/<package_name>/files`
 *
 * @receiver Fragment instance.
 * @return Absolute files directory path as [String].
 *
 * ```kotlin
 * val filePath = fragment.fileDirPath
 * ```
 */
val Fragment.fileDirPath: String
    get() = requireActivity().filesDir.absolutePath

/**
 * Gets the absolute path to the app's external cache directory.
 *
 * Example path: `/Android/data/<package_name>/cache`
 *
 * @receiver Fragment instance.
 * @return External cache directory path or empty string if unavailable.
 *
 * ```kotlin
 * val externalCache = fragment.externalCacheDirPath
 * ```
 */
val Fragment.externalCacheDirPath: String
    get() = requireActivity().externalCacheDir?.absolutePath ?: ""

/**
 * Gets the absolute path to the app's external files directory.
 *
 * Example path: `/Android/data/<package_name>/files`
 *
 * @receiver Fragment instance.
 * @return External files directory path or empty string if unavailable.
 *
 * ```kotlin
 * val externalFiles = fragment.externalFileDirPath
 * ```
 */
val Fragment.externalFileDirPath: String
    get() = requireActivity().getExternalFilesDir("")?.absolutePath ?: ""

/**
 * Registers a custom [OnBackPressedCallback] for this [Fragment], bound to its view lifecycle.
 *
 * Useful for handling back button events within fragments safely, as the callback
 * is automatically removed when the fragment’s view is destroyed.
 *
 * @receiver Fragment where the callback is registered.
 * @param callback The [OnBackPressedCallback] instance that defines the back press logic.
 *
 * @throws IllegalStateException if the fragment is not currently attached to an activity.
 *
 * ```kotlin
 *  /** BlackFragment::class */
 *  addOnBackPressedCallback {
 *         // Handle back press in activity
 *   }
 * ```
 */
fun Fragment.addOnBackPressedCallback(enabled: Boolean = true, callback: OnBackPressedCallback.() -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(this, enabled, callback)
}


/**
 * Retrieves a color value from the current theme's attributes.
 *
 * ```kotlin
 * // BlackFragment.kt
 * val color = customColorResource(R.attr.colorPrimary, Color.RED)
 * ```
 *
 * @param idAttrRes The attribute resource ID representing the color to fetch.
 * @param fallbackColor The color to return if the attribute is not found or cannot be resolved. Default is 0 (transparent).
 *
 * @return The resolved color integer (ARGB), or [fallbackColor] if attribute resolution fails.
 */
fun Fragment.customColorResource(@AttrRes idAttrRes: Int, fallbackColor: Int = 0): Int {
    return requireActivity().customColorResource(idAttrRes = idAttrRes, fallbackColor = fallbackColor)
}


/**
 * Hides the soft keyboard if any view in the Activity currently has focus.
 *
 * @receiver Fragment where keyboard will be hidden.
 *
 */
fun Fragment.hideSoftKeyboard() {
    requireActivity().hideSoftKeyboard()
}


/**
 * Returns a properly formatted display text from the given [value], or a default string resource
 * if the input is `null`, blank, or empty.
 *
 * Each word in the input is capitalized to improve display consistency.
 *
 * @param value The original string (e.g., a name or label), which may be null, blank, or improperly formatted.
 * @param default A string resource to use as fallback when [value] is null or blank.
 * @return A formatted string with each word capitalized, or the fallback string.
 *
 * Example usage:
 * ```
 * /* FragmentClass */
 *
 * val rawInput: String? = "   john doe"
 * val displayText = getDisplayText(rawInput)
 * // Result: "John Doe"
 *
 * val emptyInput: String? = null
 * val displayText = getDisplayText(emptyInput)
 * // Result: "Unknown"
 * ```
 * @see com.github.leodan11.k_extensions.core.R.string.label_text_unknown for the fallback string resource.
 */
fun Fragment.getDisplayText(value: String?, @StringRes default: Int = com.github.leodan11.k_extensions.core.R.string.label_text_unknown): String {
    return requireActivity().getDisplayText(value, default)
}


/**
 * Gets the version code of the application.
 *
 * @receiver Fragment instance.
 * @param pkgName Optional package name. Defaults to the current app package.
 * @return Version code as [Long].
 *
 * ```kotlin
 * val versionCode = fragment.getVersionCode()
 * ```
 */
fun Fragment.getVersionCode(pkgName: String = requireActivity().packageName): Long {
    return requireActivity().getVersionCode(pkgName)
}

/**
 * Gets the version name of the application.
 *
 * @receiver Fragment instance.
 * @param pkgName Optional package name. Defaults to the current app package.
 * @return Version name as [String].
 *
 * ```kotlin
 * val versionName = fragment.getVersionName()
 * ```
 */
fun Fragment.getVersionName(pkgName: String = requireActivity().packageName): String {
    return requireActivity().getVersionName(pkgName)
}

/**
 * Checks if the internet connection is currently available.
 *
 * This is a suspend function that returns the current internet connectivity status
 * as a [Boolean]. It optionally takes a [CoroutineScope] to manage the lifecycle of
 * the underlying flow subscription. If no scope is provided, a new one is created
 * using [Dispatchers.IO].
 *
 * @param coroutineScope An optional [CoroutineScope] to use for collecting the internet detection flow.
 *                       If null, a new scope is created internally.
 * @receiver The [Context] used to access system services for internet detection.
 * @return [Boolean] indicating whether the internet connection is currently available.
 * @throws CancellationException if the coroutine scope is cancelled during execution.
 * @since 2.2.1
 */
suspend fun Fragment.internetOn(coroutineScope: CoroutineScope? = null): Boolean {
    return requireActivity().internetOn(coroutineScope = coroutineScope)
}


/**
 * Launches a new Activity from a Fragment.
 *
 * @receiver Fragment from which the new Activity will be started.
 * @param page The Activity class to launch.
 * @param finishCurrent Whether to finish the current Activity. Default is true.
 * @param block Lambda to configure the [Intent] before launching.
 *
 *
 * ```kotlin
 * fragment.startNewPage(DetailActivity::class.java) {
 *     putExtra("id", 123)
 * }
 * ```
 */
fun Fragment.startNewPage(
    page: Class<*>,
    finishCurrent: Boolean = true,
    block: Intent.() -> Unit = {}
) {
    requireActivity().startNewPage(page, finishCurrent, block)
}


/**
 * Returns a localized string representing elapsed time in selectable units.
 *
 * You can choose which units to show (days, hours, minutes, seconds).
 * Units with zero value will be omitted unless explicitly included.
 *
 * Example:
 * ```kotlin
 * context.toElapsedTimeString(days = 1, hours = 2, minutes = 3, seconds = 4)
 * // Default: "1 day, 2 hours, 3 minutes and 4 seconds"
 *
 * context.toElapsedTimeString(days = 1, hours = 2, minutes = 3, showUnits = listOf(UnitType.DAYS, UnitType.HOURS))
 * // Only shows days and hours: "1 day and 2 hours"
 * ```
 *
 * @param days Number of elapsed days
 * @param hours Number of elapsed hours
 * @param minutes Number of elapsed minutes
 * @param seconds Number of elapsed seconds
 * @param showUnits List of units to display. Default shows all non-zero units.
 * @return A localized, formatted string describing the elapsed time
 *
 * @since 2.2.2
 */
fun Fragment.toElapsedTimeString(days: Int = 0, hours: Int = 0, minutes: Int = 0, seconds: Int = 0, showUnits: List<UnitType> = listOf(UnitType.DAYS, UnitType.HOURS, UnitType.MINUTES, UnitType.SECONDS)): String {
    return requireActivity().toElapsedTimeString(days = days, hours = hours, minutes = minutes, seconds = seconds, showUnits = showUnits)
}

/**
 * Validates that the input in an [AutoCompleteTextView] is not empty.
 * Uses the default error message: "This field cannot be left empty".
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputAutoComplete The [AutoCompleteTextView] to validate.
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputAutoComplete)
}

/**
 * Validates that the input in an [AutoCompleteTextView] is not empty.
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputAutoComplete The [AutoCompleteTextView] to validate.
 * @param message Error message as a [String].
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    message: String
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputAutoComplete, message)
}

/**
 * Validates that the input in an [AutoCompleteTextView] is not empty.
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputAutoComplete The [AutoCompleteTextView] to validate.
 * @param message Error message as a string resource [Int].
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    @StringRes message: Int
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputAutoComplete, message)
}

/**
 * Validates that the input in an [EditText] is not empty.
 * Uses the default error message: "This field cannot be left empty".
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputEditText The [EditText] to validate.
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputEditText)
}

/**
 * Validates that the input in an [EditText] is not empty.
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputEditText The [EditText] to validate.
 * @param message Error message as a [String].
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    message: String
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputEditText, message)
}

/**
 * Validates that the input in an [EditText] is not empty.
 *
 * @receiver Fragment where the view is located.
 * @param inputLayout [TextInputLayout] containing the field.
 * @param inputEditText The [EditText] to validate.
 * @param message Error message as a string resource [Int].
 * @return `true` if valid, `false` otherwise.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    @StringRes message: Int
): Boolean {
    return requireActivity().validateTextField(inputLayout, inputEditText, message)
}