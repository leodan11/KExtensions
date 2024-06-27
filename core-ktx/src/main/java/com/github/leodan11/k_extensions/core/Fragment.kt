package com.github.leodan11.k_extensions.core

import androidx.fragment.app.Fragment

/**
 * Get the application file directory
 *
 * Application file directory ("/data/data/<package name>/files")
 */
val Fragment.fileDirPath: String
    get() = requireActivity().fileDirPath

/**
 * Get the application cache directory
 *
 * Application cache directory ("/data/data/<package name>/cache")
 */
val Fragment.cacheDirPath: String
    get() = requireActivity().cacheDirPath

/**
 * Get the application external file directory
 *
 * Application file directory ("/Android/data/<package name>/files")
 */
val Fragment.externalFileDirPath: String
    get() = requireActivity().externalFileDirPath

/**
 * Get the application external cache directory
 *
 * Application cache directory ("/Android/data/<package name>/cache")
 */
val Fragment.externalCacheDirPath: String
    get() = requireActivity().externalCacheDirPath

/**
 * Get the application version code
 *
 * @param pkgName [String] - Package name
 * @return [Long] - Version code
 *
 */
fun Fragment.getVersionCode(pkgName: String = requireActivity().packageName.toString()): Long = requireActivity().getVersionCode(pkgName)


/**
 * Get the application version name
 *
 * @param pkgName [String] - Package name
 * @return [String] - Version name
 */
fun Fragment.getVersionName(pkgName: String = requireActivity().packageName.toString()): String = requireActivity().getVersionName(pkgName)
