package com.github.leodan11.k_extensions.permission

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import com.github.leodan11.k_extensions.permission.content.PermissionResult


/**
 * Returns a [PermissionManager] tied to this [ComponentActivity].
 *
 * @since 3.0.1
 */
fun ComponentActivity.permissionManager(): PermissionManager {
    return PermissionManager(this)
}


/**
 * Returns a [PermissionManager] tied to this [Fragment].
 *
 * Uses the hosting Activity as the permission context.
 *
 * @since 3.0.1
 */
fun Fragment.permissionManager(): PermissionManager {
    return PermissionManager(requireActivity())
}


/**
 * Requests the camera permission.
 *
 * @param onResult Callback that returns the permission result.
 *
 * @since 3.0.1
 */
fun PermissionManager.camera(onResult: (PermissionResult) -> Unit) {
    request(Manifest.permission.CAMERA, onResult = onResult)
}


/**
 * Requests fine and coarse location permissions.
 *
 * @param onResult Callback that returns the permission result.
 *
 * @since 3.0.1
 */
fun PermissionManager.location(onResult: (PermissionResult) -> Unit) {
    request(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        onResult = onResult
    )
}
