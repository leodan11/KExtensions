package com.github.leodan11.k_extensions.core

import android.util.Base64

/**
 * ByteArray encode to string
 *
 * @param flags By default [Base64.DEFAULT]
 * @return [String]
 */
fun ByteArray.encodeToString(flags: Int = Base64.DEFAULT): String = Base64.encodeToString(this, flags)


/**
 * ByteArray to string hexadecimal
 *
 * @param separator By default, empty String
 * @return [String]
 */
fun ByteArray.toHexString(separator: String = ""): String = joinToString(separator) { "%02x".format(it) }