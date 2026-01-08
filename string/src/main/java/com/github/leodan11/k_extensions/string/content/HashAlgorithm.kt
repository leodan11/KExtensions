package com.github.leodan11.k_extensions.string.content

sealed class HashAlgorithm(val value: String) {
    object MD5 : HashAlgorithm("MD5")
    object SHA1 : HashAlgorithm("SHA-1")
    object SHA256 : HashAlgorithm("SHA-256")
    object SHA512 : HashAlgorithm("SHA-512")
    data class Custom(val algorithm: String) : HashAlgorithm(algorithm)
}