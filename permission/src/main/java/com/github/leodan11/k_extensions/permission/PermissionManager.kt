package com.github.leodan11.k_extensions.permission

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.github.leodan11.k_extensions.permission.content.PermissionResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


/**
 * A manager class for handling Android runtime permissions.
 *
 * This class provides a simple and consistent API to request permissions using
 * either callbacks or Kotlin coroutines. It abstracts the complexity of the
 * Activity Result API and delivers a structured result via [PermissionResult].
 *
 * Features:
 * - Request single or multiple permissions
 * - Detect permanently denied permissions
 * - Coroutine-friendly API
 *
 * Internally, it uses the Activity Result API to ensure lifecycle-safe
 * permission handling.
 *
 *
 * Example using callback:
 * ```kotlin
 * val manager = PermissionManager(this)
 *
 * manager.request(
 *     android.Manifest.permission.CAMERA,
 *     android.Manifest.permission.RECORD_AUDIO
 * ) { result ->
 *     if (result.allGranted) {
 *         // All permissions granted
 *     } else {
 *         // Handle denied or permanently denied permissions
 *     }
 * }
 * ```
 *
 * Example using coroutines:
 * ```kotlin
 * val manager = PermissionManager(this)
 *
 * lifecycleScope.launch {
 *     val result = manager.requestSuspend(
 *         android.Manifest.permission.CAMERA
 *     )
 *
 *     if (result.allGranted) {
 *         // Permission granted
 *     }
 * }
 * ```
 *
 * @property activity The [ComponentActivity] used to register the permission launcher.
 *
 * @constructor Creates a new instance of [PermissionManager].
 *
 * @see PermissionResult
 *
 * @since 3.0.1
 *
 */
class PermissionManager(private val activity: ComponentActivity) {

    private var callback: ((PermissionResult) -> Unit)? = null

    private val launcher = activity.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->

        val granted = mutableListOf<String>()
        val denied = mutableListOf<String>()
        val permanent = mutableListOf<String>()

        result.forEach { (permission, isGranted) ->
            if (isGranted) {
                granted.add(permission)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                    permanent.add(permission)
                } else {
                    denied.add(permission)
                }
            }
        }

        callback?.invoke(
            PermissionResult(
                allGranted = denied.isEmpty() && permanent.isEmpty(),
                granted = granted,
                denied = denied,
                permanentlyDenied = permanent
            )
        )
    }

    /**
     * Requests one or more permissions.
     *
     * @param permissions Permissions to request.
     * @param onResult Callback with final result.
     *
     * @since 3.0.1
     */
    fun request(vararg permissions: String, onResult: (PermissionResult) -> Unit) {
        callback = onResult

        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            onResult(
                PermissionResult(
                    allGranted = true,
                    granted = permissions.toList(),
                    denied = emptyList(),
                    permanentlyDenied = emptyList()
                )
            )
        } else {
            launcher.launch(notGranted.toTypedArray())
        }
    }

    /**
     * Suspends the current coroutine and requests the given permissions.
     *
     * This is a coroutine-based alternative to [request]. The function will suspend
     * until the user responds to the permission dialog and then return a
     * [PermissionResult].
     *
     * @param permissions The permissions to request.
     * @return The result of the permission request.
     *
     * @since 3.0.1
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun requestSuspend(vararg permissions: String): PermissionResult = suspendCancellableCoroutine { cont ->
        request(*permissions) { result ->
            if (cont.isActive) {
                cont.resume(result)
            }
        }
    }

    /**
     * Opens the application's system settings screen.
     *
     * This is typically used when one or more permissions have been permanently
     * denied and must be manually enabled by the user.
     *
     * @since 3.0.1
     */
    fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
        }
        activity.startActivity(intent)
    }

    /**
     * Returns whether a rationale should be shown for the given permission.
     *
     * @param permission The permission to check.
     * @return `true` if the system recommends showing additional context to the user.
     *
     * @since 3.0.1
     */
    fun shouldShowRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    }

}
