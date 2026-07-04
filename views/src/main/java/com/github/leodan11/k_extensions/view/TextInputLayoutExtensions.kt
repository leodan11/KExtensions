


/**
 * Hides the end icon and removes any end icon mode currently applied.
 *
 * This is a convenience extension around
 * [TextInputLayout.END_ICON_NONE].
 *
 * ## Example
 * ```kotlin
 * textInputLayout.hideEndIcon()
 * ```
 */
fun TextInputLayout.hideEndIcon() {
    endIconMode = TextInputLayout.END_ICON_NONE
    endIconContentDescription = null
}

/**
 * Displays a custom end icon and sets its content description
 * using a string resource.
 *
 * The end icon mode is automatically switched to
 * [TextInputLayout.END_ICON_CUSTOM].
 *
 * @param iconRes Drawable resource used as the end icon.
 * @param descriptionRes String resource used as the content description
 * for accessibility services.
 *
 * ## Example
 * ```kotlin
 * textInputLayout.showEndIcon(
 *     iconRes = R.drawable.ic_search,
 *     descriptionRes = R.string.search
 * )
 * ```
 */
fun TextInputLayout.showEndIcon(
    @DrawableRes iconRes: Int,
    @StringRes descriptionRes: Int
) {
    showEndIcon(
        iconRes = iconRes,
        description = context.getString(descriptionRes)
    )
}

/**
 * Displays a custom end icon and sets its content description.
 *
 * The end icon mode is automatically switched to
 * [TextInputLayout.END_ICON_CUSTOM].
 *
 * @param iconRes Drawable resource used as the end icon.
 * @param description Content description used by accessibility services.
 *
 * ## Example
 * ```kotlin
 * textInputLayout.showEndIcon(
 *     iconRes = R.drawable.ic_search,
 *     description = "Search"
 * )
 * ```
 */
fun TextInputLayout.showEndIcon(
    @DrawableRes iconRes: Int,
    description: String
) {
    endIconMode = TextInputLayout.END_ICON_CUSTOM
    setEndIconDrawable(iconRes)
    endIconContentDescription = description
}

/**
 * Displays a start icon and sets its content description
 * using a string resource.
 *
 * @param iconRes Drawable resource used as the start icon.
 * @param descriptionRes String resource used as the content description
 * for accessibility services.
 *
 * ## Example
 * ```kotlin
 * textInputLayout.showStartIcon(
 *     iconRes = R.drawable.ic_location,
 *     descriptionRes = R.string.location
 * )
 * ```
 */
fun TextInputLayout.showStartIcon(
    @DrawableRes iconRes: Int,
    @StringRes descriptionRes: Int
) {
    showStartIcon(
        iconRes = iconRes,
        description = context.getString(descriptionRes)
    )
}

/**
 * Displays a start icon and sets its content description.
 *
 * @param iconRes Drawable resource used as the start icon.
 * @param description Content description used by accessibility services.
 *
 * ## Example
 * ```kotlin
 * textInputLayout.showStartIcon(
 *     iconRes = R.drawable.ic_location,
 *     description = "Location"
 * )
 * ```
 */
fun TextInputLayout.showStartIcon(
    @DrawableRes iconRes: Int,
    description: String
) {
    setStartIconDrawable(iconRes)
    startIconContentDescription = description
}

/**
 * Removes the currently displayed start icon.
 *
 * After calling this function, no drawable will be shown in the
 * start icon position of the [TextInputLayout].
 *
 * This operation does not modify any previously assigned
 * content description.
 *
 * ## Example
 * ```kotlin
 * textInputLayout.hideStartIcon()
 * ```
 */
fun TextInputLayout.hideStartIcon() {
    startIconDrawable = null
    startIconContentDescription = null
}

