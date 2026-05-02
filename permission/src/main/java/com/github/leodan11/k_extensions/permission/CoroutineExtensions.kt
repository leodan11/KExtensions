package com.github.leodan11.k_extensions.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.github.leodan11.k_extensions.permission.content.PermissionResult


/**
 * Suspends the current coroutine and requests the given permissions
 * from this [ComponentActivity].
 *
 * This is a convenience extension that internally uses [PermissionManager]
 * to perform the request.
 *
 * @param permissions The permissions to request.
 * @return The result of the permission request.
 *
 * Example:
 * ```kotlin
 * val result = askPermissions(Manifest.permission.CAMERA)
 *
 * if (result.allGranted) {
 *     // Permission granted
 * }
 * ```
 *
 * @since 3.0.1
 */
suspend fun ComponentActivity.askPermissions(vararg permissions: String): PermissionResult {
    return permissionManager().requestSuspend(*permissions)
}


/**
 * Suspends the current coroutine and requests the given permissions
 * from this [ComponentActivity].
 *
 * This is a convenience extension that internally uses [PermissionManager]
 * to perform the request.
 *
 * @param permissions The permissions to request.
 * @return The result of the permission request.
 *
 * Example:
 * ```kotlin
 * val result = askPermissions(Manifest.permission.CAMERA)
 *
 * if (result.allGranted) {
 *     // Permission granted
 * }
 * ```
 *
 * @since 3.0.1
 */
suspend fun Fragment.askPermissions(vararg permissions: String): PermissionResult {
    return permissionManager().requestSuspend(*permissions)
}


/**
 * Suspends the current coroutine and requests the camera permission.
 *
 * This is a shorthand for calling [askPermissions] with
 * [Manifest.permission.CAMERA].
 *
 * @return The result of the permission request.
 *
 * Example:
 * ```kotlin
 * val result = askCamera()
 *
 * result.onGranted {
 *     // Open camera
 * }.onPermanentlyDenied {
 *     openSettings()
 * }
 * ```
 *
 * @since 3.0.1
 */
suspend fun ComponentActivity.askCamera(): PermissionResult {
    return this.askPermissions(Manifest.permission.CAMERA)
}


/**
 * Suspends the current coroutine and requests the camera permission.
 *
 * This is a shorthand for calling [askPermissions] with
 * [Manifest.permission.CAMERA].
 *
 * @return The result of the permission request.
 *
 * Example:
 * ```kotlin
 * val result = askCamera()
 *
 * result.onGranted {
 *     // Open camera
 * }.onPermanentlyDenied {
 *     openSettings()
 * }
 * ```
 *
 * @since 3.0.1
 */
suspend fun Fragment.askCamera(): PermissionResult {
    return this.askPermissions(Manifest.permission.CAMERA)
}
