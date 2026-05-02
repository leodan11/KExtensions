package com.github.leodan11.k_extensions.permission.content


/**
 * Represents the outcome of a runtime permission request.
 *
 * This data class provides a structured summary of the permission request,
 * including which permissions were granted, denied, or permanently denied.
 *
 * A permission is considered *permanently denied* when the user selects
 * "Don't ask again" or when the system determines that a rationale
 * should no longer be shown.
 *
 * In addition to raw data, this class offers helper properties and a
 * fluent API to simplify common permission handling flows.
 *
 * @property allGranted `true` if all requested permissions were granted.
 * @property granted List of permissions that were granted.
 * @property denied List of permissions that were denied but can be requested again.
 * @property permanentlyDenied List of permissions that were denied with
 * "Don't ask again" and must be enabled manually from system settings.
 *
 * Example:
 * ```kotlin
 * permissionManager.request(Manifest.permission.CAMERA) { result ->
 *     result
 *         .onGranted {
 *             // All permissions granted
 *         }
 *         .onDenied { denied ->
 *             // Show rationale to the user
 *         }
 *         .onPermanentlyDenied {
 *             permissionManager.openSettings()
 *         }
 * }
 * ```
 *
 * @since 3.0.1
 */
data class PermissionResult(
    val allGranted: Boolean,
    val granted: List<String>,
    val denied: List<String>,
    val permanentlyDenied: List<String>
) {

    /**
     * Returns `true` if there are permissions that should show a rationale to the user.
     *
     * @since 3.0.1
     */
    val shouldShowRationale: Boolean
        get() = denied.isNotEmpty()

    /**
     * Returns `true` if the user should be redirected to system settings.
     *
     * @since 3.0.1
     */
    val shouldOpenSettings: Boolean
        get() = permanentlyDenied.isNotEmpty()

    /**
     * Executes [block] if all permissions were granted.
     *
     * @since 3.0.1
     */
    inline fun onGranted(block: () -> Unit): PermissionResult {
        if (allGranted) block()
        return this
    }

    /**
     * Executes [block] if there are denied permissions that can be requested again.
     *
     * @param block Provides the list of denied permissions.
     *
     * @since 3.0.1
     */
    inline fun onDenied(block: (List<String>) -> Unit): PermissionResult {
        if (denied.isNotEmpty()) block(denied)
        return this
    }

    /**
     * Executes [block] if there are permanently denied permissions.
     *
     * These permissions require manual enabling from system settings.
     *
     * @param block Provides the list of permanently denied permissions.
     *
     * @since 3.0.1
     */
    inline fun onPermanentlyDenied(block: (List<String>) -> Unit): PermissionResult {
        if (permanentlyDenied.isNotEmpty()) block(permanentlyDenied)
        return this
    }

    /**
     * Executes [block] if not all permissions were granted.
     *
     * @since 3.0.1
     */
    inline fun onAnyDenied(block: () -> Unit): PermissionResult {
        if (!allGranted) block()
        return this
    }
}
