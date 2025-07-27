package com.github.leodan11.k_extensions.context

import android.content.Intent
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout

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
 *  addOnBackPressedCallback(object : OnBackPressedCallback(true) {
 *          override fun handleOnBackPressed() {
 *               // Handle back press in activity
 *          }
 *   })
 * ```
 */
fun Fragment.addOnBackPressedCallback(callback: OnBackPressedCallback) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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