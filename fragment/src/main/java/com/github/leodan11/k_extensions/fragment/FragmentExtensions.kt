@file:Suppress("DEPRECATION")

package com.github.leodan11.k_extensions.fragment

import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment

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
