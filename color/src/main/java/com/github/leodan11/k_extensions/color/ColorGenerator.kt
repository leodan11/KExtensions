package com.github.leodan11.k_extensions.color

import kotlin.math.abs
import kotlin.random.Random

/**
 * A utility class that generates random colors from a predefined list of colors.
 *
 * This class allows generating a random color, accessing a list of available colors, and selecting
 * a color based on a given key. It can be used to generate consistent color selections based on keys.
 *
 * Example usage:
 * ```kotlin
 * val generator = ColorGenerator.DEFAULT
 * val randomColor = generator.randomColor
 * val colorForKey = generator.getColorBasedOnKey("someKey")
 * ```
 *
 * @property mColors The list of colors used for generation.
 * @property mRandom A random number generator to select random colors.
 */
class ColorGenerator private constructor(private val mColors: List<Int>) {

    // Random generator initialized with current time for seeding.
    private val mRandom: Random = Random(System.currentTimeMillis())

    /**
     * Returns the list of available colors in the generator.
     *
     * @return A list of [Int] representing colors in ARGB format.
     */
    val listColors: List<Int>
        get() = mColors

    /**
     * Returns a random color from the available colors.
     *
     * @return A randomly selected color from the list of available colors.
     */
    val randomColor: Int
        get() = mColors[mRandom.nextInt(mColors.size)]

    /**
     * Returns a color based on the provided key. The color is selected based on the hash code of the key.
     * This ensures that the same key will always return the same color.
     *
     * @param key The key used to select a color.
     * @return A color that corresponds to the provided key.
     */
    fun getColorBasedOnKey(key: Any): Int {
        // Uses the absolute value of the hash code to ensure positive values
        return mColors[abs(key.hashCode()) % mColors.size]
    }

    companion object {

        /** Default Color Generator with a predefined set of colors. */
        @JvmStatic
        val DEFAULT: ColorGenerator

        /** Material Color Generator with a predefined set of colors. */
        @JvmStatic
        val MATERIAL: ColorGenerator

        init {
            // Predefined color lists for DEFAULT and MATERIAL color sets.
            DEFAULT = create(
                listOf(
                    -0xe9c9c,
                    -0xa7aa7,
                    -0x65bc2,
                    -0x1b39d2,
                    -0x98408c,
                    -0xa65d42,
                    -0xdf6c33,
                    -0x529d59,
                    -0x7fa87f
                )
            )

            MATERIAL = create(
                listOf(
                    -0x1a8c8d,
                    -0xf9d6e,
                    -0x459738,
                    -0x6a8a33,
                    -0x867935,
                    -0x9b4a0a,
                    -0xb03c09,
                    -0xb22f1f,
                    -0xb24954,
                    -0x7e387c,
                    -0x512a7f,
                    -0x759b,
                    -0x2b1ea9,
                    -0x2ab1,
                    -0x48b3,
                    -0x5e7781,
                    -0x6f5b52
                )
            )
        }

        /**
         * Creates a new [ColorGenerator] instance with a custom list of colors.
         *
         * @param colorList A list of colors to be used by the generator.
         * @return A new [ColorGenerator] instance with the provided color list.
         */
        @JvmStatic
        fun create(colorList: List<Int>): ColorGenerator {
            return ColorGenerator(colorList)
        }
    }
}
