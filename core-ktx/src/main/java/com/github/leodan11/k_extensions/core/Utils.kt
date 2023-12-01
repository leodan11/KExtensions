package com.github.leodan11.k_extensions.core

import android.util.Base64

/**
 * Base64 encode to string
 *
 * @param flags By default [Base64.DEFAULT]
 * @return [String]
 */
fun ByteArray.base64EncodeToString(flags: Int = Base64.DEFAULT): String = Base64.encodeToString(this, flags)