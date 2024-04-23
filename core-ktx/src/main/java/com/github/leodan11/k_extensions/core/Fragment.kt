package com.github.leodan11.k_extensions.core

import androidx.fragment.app.Fragment

val Fragment.fileDirPath: String
    get() = activity?.fileDirPath ?: ""

val Fragment.cacheDirPath: String
    get() = activity?.cacheDirPath ?: ""

val Fragment.externalFileDirPath: String
    get() = activity?.externalFileDirPath ?: ""

val Fragment.externalCacheDirPath: String
    get() = activity?.externalCacheDirPath ?: ""