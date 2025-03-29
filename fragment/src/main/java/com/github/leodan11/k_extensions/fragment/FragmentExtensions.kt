@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.github.leodan11.k_extensions.core.tag
import com.google.android.material.textfield.TextInputLayout

/**
 * Get the application file directory
 *
 * Application file directory ("/data/data/<package name>/files")
 *
 * @return [String] - File directory
 *
 */
val Fragment.fileDirPath: String
    get() = requireActivity().filesDir.absolutePath

/**
 * Get the application cache directory
 *
 * Application cache directory ("/data/data/<package name>/cache")
 *
 * @return [String] - Cache directory
 *
 */
val Fragment.cacheDirPath: String
    get() = requireActivity().cacheDir.absolutePath

/**
 * Get the application external file directory
 *
 * Application file directory ("/Android/data/<package name>/files")
 *
 * @return [String] - External file directory
 *
 */
val Fragment.externalFileDirPath: String
    get() = requireActivity().getExternalFilesDir("")?.absolutePath ?: ""

/**
 * Get the application external cache directory
 *
 * Application cache directory ("/Android/data/<package name>/cache")
 *
 * @return [String] - External cache directory
 *
 */
val Fragment.externalCacheDirPath: String
    get() = requireActivity().externalCacheDir?.absolutePath ?: ""

/**
 * Get the application version code
 *
 * @param pkgName [String] - Package name
 * @return [Long] - Version code
 *
 */
fun Fragment.getVersionCode(pkgName: String = requireActivity().packageName.toString()): Long {
    if (pkgName.isBlank()) return -1
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) requireActivity().packageManager.getPackageInfo(
            pkgName,
            0
        ).longVersionCode
        else requireActivity().packageManager.getPackageInfo(pkgName, 0).versionCode.toLong()
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
fun Fragment.getVersionName(pkgName: String = requireActivity().packageName.toString()): String {
    if (pkgName.isBlank()) return ""
    return try {
        requireActivity().packageManager.getPackageInfo(pkgName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        ""
    }
}

/**
 * Validate Text field has data. Default error message
 *  - `This field cannot be left empty`
 *
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 *
 * @return [Boolean] `true` or `false`.
 *
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText
): Boolean {
    val errorDefault = this.getString(com.github.leodan11.k_extensions.core.R.string.label_this_field_cannot_be_left_empty)
    return this.validateTextField(inputLayout, inputEditText, errorDefault)
}


/**
 * Validate Text field has data
 *
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 * @param message [String] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputEditText: EditText,
    message: String
): Boolean {
    Log.i(this.tag(), "Fragment::validateTextField()")
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
 * Validate Text field has data. Default error message
 *  *  - `This field cannot be left empty`
 *
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 *
 * @return [Boolean] `true` or `false`.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView
): Boolean {
    val errorDefault = this.getString(com.github.leodan11.k_extensions.core.R.string.label_this_field_cannot_be_left_empty)
    return this.validateTextField(inputLayout, inputAutoComplete, errorDefault)
}


/**
 * Validate Text field has data
 *
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 * @param message [String] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Fragment.validateTextField(
    inputLayout: TextInputLayout,
    inputAutoComplete: AutoCompleteTextView,
    message: String
): Boolean {
    Log.i(this.tag(), "Fragment::validateTextField()")
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
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputEditText Text field [EditText].
 * @param message [Int] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Fragment.validateTextField(
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
 * @receiver [Fragment] required current fragment
 *
 * @param inputLayout Parent element [TextInputLayout].
 * @param inputAutoComplete Text field [AutoCompleteTextView].
 * @param message [Int] Error to be displayed on the element.
 *
 * @return [Boolean] `true` or `false`.
 */
fun Fragment.validateTextField(
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
