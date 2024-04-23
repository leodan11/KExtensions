package com.github.leodan11.k_extensions.core

import android.os.Environment
import android.util.Log
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.channels.FileChannel


/**
 * Copy file
 *
 * @param toFile [FileOutputStream]
 */
fun FileInputStream.copyFile(toFile: FileOutputStream) {
    try {
        var fromChannel: FileChannel? = null
        var toChannel: FileChannel? = null

        try {
            fromChannel = this.channel
            toChannel = toFile.channel

            fromChannel.transferTo(0, fromChannel.size(), toChannel)
        } finally {
            try {
                fromChannel?.close()
            } finally {
                toChannel?.close()
            }
        }
        Log.i("Copy File", "Completed")
    } catch (exception: Exception) {
        exception.printStackTrace()
        throw Exception("Exception copy File")
    }
}


/**
 * Get a public download folder path
 */
val publicDownloadDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath

/**
 * Get a public photo folder path
 */
val publicDCIMDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

/**
 * Get the public image folder path
 */
val publicPictureDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath

/**
 * Get the public music folder path
 */
val publicMusicDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath

/**
 * Get a public movie folder path
 */
val publicMovieDir: String
    get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).absolutePath


/**
 * Is the memory card mounted?
 */
val isExternalStorageWritable: Boolean
    get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

/**
 * Get a file object by file path
 *
 * @param filePath
 * @return nullable
 */
fun getFileByPath(filePath: String): File? = if (filePath.isBlank()) null else File(filePath)

/**
 * Determine whether the file exists
 *
 */
val File.isFileExists: Boolean get() = exists() && isFile

/**
 * Determine whether the file exists
 *
 * @param filePath
 */
fun isFileExists(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.isFileExists ?: false
}

/**
 * Determine whether the folder exists
 *
 */
val File.isDirExists: Boolean get() = exists() && isDirectory

/**
 * Determine whether the folder exists
 *
 * @param filePath
 */
fun isDirExists(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.isDirExists ?: false
}

/**
 * Determine whether the directory exists. If it does not exist, determine whether it was created successfully.
 *
 * @return true The folder exists or was created successfully, false The folder does not exist, or creation failed
 */
fun File.createOrExistsDir(): Boolean =
    // If it exists, it returns true if it is a directory, false if it is a file, and whether it was created successfully if it does not exist.
    if (exists()) isDirectory else mkdirs()

/**
 * Determine whether the directory exists. If it does not exist, determine whether it was created successfully.
 *
 * @param filePath
 * @return true The folder exists or was created successfully false The path is invalid, the folder does not exist, or the creation failed
 */
fun createOrExistsDir(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.createOrExistsDir() ?: false
}

/**
 * Determine whether the file exists. If it does not exist, determine whether it was created successfully.
 *
 * @return true The file exists or was created successfully, false File does not exist, or creation failed
 */
fun File.createOrExistsFile(): Boolean {
    if (exists()) return isFile
    if (parentFile?.createOrExistsDir() != true) return false

    return try {
        createNewFile()
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * Determine whether the file exists. If it does not exist, determine whether it was created successfully.
 *
 * @param filePath
 * @return true The file exists or was created successfully, false The path is invalid, the file does not exist, or the creation failed
 */
fun createOrExistsFile(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.createOrExistsFile() ?: false
}

/**
 * Get folder directory size
 *
 * @return File size unit: B, KB, MB, GB
 */
val File.dirSize: String
    get() = if (dirLength == -1L) "" else byte2FitMemorySize(dirLength)

/**
 * Get folder directory size
 *
 * @param dirPath
 * @return File size unit: B, KB, MB, GB
 */
fun getDirSize(dirPath: String): String {
    val len = getDirLength(dirPath)
    return if (len == -1L) "" else byte2FitMemorySize(len)
}

/**
 * Get directory length
 *
 * @return Directory length
 */
val File.dirLength: Long
    get() {
        if (!isDirExists) return -1
        var len: Long = 0
        val files: Array<File>? = listFiles()
        files?.forEach {
            len += if (it.isDirectory) it.dirLength else it.length()
        }
        return len
    }

/**
 * Get directory length
 *
 * @param dirPath
 * @return Directory length
 */
fun getDirLength(dirPath: String): Long {
    val dir = getFileByPath(dirPath)
    return dir?.dirLength ?: -1
}

/**
 * Get file size
 *
 * @return File size unit: B, KB, MB, GB
 */
val File.fileSize: String
    get() = if (fileLength == -1L) "" else byte2FitMemorySize(fileLength)

/**
 * Get file size
 *
 * @param filePath
 * @return File size unit: B, KB, MB, GB
 */
fun getFileSize(filePath: String): String {
    val len = getFileLength(filePath)
    return if (len == -1L) "" else byte2FitMemorySize(len)
}

/**
 * Get file length
 *
 * @return File length
 */
val File.fileLength: Long get() = if (!isFileExists) -1 else length()

/**
 * Get file length
 *
 * @param filePath
 * @return File length
 */
fun getFileLength(filePath: String): Long {
    val file = getFileByPath(filePath)
    return file?.fileLength ?: -1
}

/**
 * Convert the number of bytes to the appropriate memory size
 *
 * Keep 3 decimal places
 *
 * @param byteNum Number of bytes
 * @return Suitable memory size
 */
private fun byte2FitMemorySize(byteNum: Long): String = when {
    byteNum < 0 -> "shouldn't be less than zero!"
    byteNum < 1024 -> String.format("%.3fB", byteNum.toDouble() + 0.0005)
    byteNum < 1048576 -> String.format("%.3fKB", byteNum.toDouble() / 1024 + 0.0005)
    byteNum < 1073741824 -> String.format("%.3fMB", byteNum.toDouble() / 1048576 + 0.0005)
    else -> String.format("%.3fGB", byteNum.toDouble() / 1073741824 + 0.0005)
}

/**
 * Get the longest directory in the full path
 *
 * @return longest directory
 */
val File.dirName: String get() = getDirName(path)

/**
 * Get the longest directory in the full path
 *
 * @param filePath
 * @return longest directory
 */
fun getDirName(filePath: String): String =
    if (filePath.isBlank()) filePath else filePath.substringBeforeLast(File.separator)

/**
 * Get the file name in the full path
 *
 * @return file name
 */
val File.fileName: String get() = getFileName(path)

/**
 * Get the file name in the full path
 *
 * @param filePath
 * @return file name
 */
fun getFileName(filePath: String): String =
    if (filePath.isBlank()) filePath else filePath.substringAfterLast(File.separator)

/*
  ---------- Read/write files ----------
 */
/**
 * Write string to file
 *
 * @param content Write content
 * @param append  Whether to append at the end of the file
 * @return true Write successful false Write failed
 * @exception Exception
 */
fun File.writeStringAsFile(content: String, append: Boolean = false): Boolean {
    if (!createOrExistsFile()) return false

    BufferedWriter(FileWriter(this, append)).use {
        it.write(content)
        return true
    }
}

/**
 * Write string to file
 *
 * @param filePath    document
 * @param content Write content
 * @param append  Whether to append at the end of the file
 * @return true Write successfully false Write failed
 * @exception Exception
 */
fun writeStringAsFile(filePath: String, content: String, append: Boolean = false): Boolean {
    val file = getFileByPath(filePath)
    return file?.writeStringAsFile(content, append) ?: false
}

/**
 * Write input stream to file
 *
 * @param inputStream
 * @param append
 * @return true  false
 */
fun File.writeISAsFile(inputStream: InputStream, append: Boolean = false): Boolean {
    if (!createOrExistsFile()) return false

    BufferedOutputStream(FileOutputStream(this, append)).use {
        inputStream.copyTo(it)
        return true
    }
}

/**
 * Write input stream to file
 *
 * @param filePath
 * @param inputStream
 * @param append
 * @return true  false
 */
fun writeISAsFile(filePath: String, inputStream: InputStream, append: Boolean = false): Boolean {
    val file = getFileByPath(filePath)

    return file?.writeISAsFile(inputStream, append) ?: false
}

/**
 * Read file into string
 *
 * @param charsetName Encoding format
 * @return string
 */
fun File.readFileAsString(charsetName: String = ""): String {
    if (!isFileExists) return ""
    val reader: BufferedReader =
        if (charsetName.isBlank()) BufferedReader(InputStreamReader(FileInputStream(this))) else
            BufferedReader(InputStreamReader(FileInputStream(this), charsetName))

    val sb = StringBuilder()
    reader.forEachLine {
        if (sb.isNotBlank()) sb.appendLine()
        sb.append(it)
    }
    return sb.toString()
}

/**
 * Read file into string
 *
 * @param filePath    file a path
 * @param charsetName Encoding format
 * @return string
 */
fun readFileAsString(filePath: String, charsetName: String = ""): String {
    val file = getFileByPath(filePath)
    return file?.readFileAsString(charsetName) ?: ""
}

/**
 * Read file into a list of strings
 * Do not use this function for huge files.
 * @param charsetName
 * @return List<String>
 */
fun File.readFileAsList(charsetName: String = ""): List<String> {
    if (!isFileExists) return emptyList()
    val reader: BufferedReader =
        if (charsetName.isBlank()) BufferedReader(InputStreamReader(FileInputStream(this))) else
            BufferedReader(InputStreamReader(FileInputStream(this), charsetName))
    return reader.readLines()
}

/**
 * Read file into a list of strings
 * Do not use this function for huge files.
 * @param filePath
 * @param charsetName
 * @return List<String>
 */
fun readFileAsList(filePath: String, charsetName: String = ""): List<String> {
    val file = getFileByPath(filePath)
    return file?.readFileAsList(charsetName) ?: emptyList()
}

/*
  ---------- thanks for https://github.com/Blankj/AndroidUtilCode ----------
 */

/**
 * Copy or move a directory (default is copy directory)
 *
 * @param destDir
 * @param isMove default false
 */
fun File.copyOrMoveDir(destDir: File, isMove: Boolean = false): Boolean {
    val srcPath = path + File.separator
    val destPath = destDir.path + File.separator
    if (destPath.contains(srcPath)) return false
    if (!exists() || !isDirectory) return false
    if (!destDir.createOrExistsDir()) return false

    val files = listFiles()
    files?.forEach {
        val destFile = File(destPath + it.name)
        if (it.isFile) {
            if (!it.copyOrMoveFile(destFile, isMove)) return false
        } else if (it.isDirectory) {
            if (!copyOrMoveDir(destFile, isMove)) return false
        }
    }

    return !isMove || deleteDir()
}

/**
 * Copy or move a directory (default is copy directory)
 *
 * @param srcDirPath
 * @param destDirPath
 * @param isMove default false
 */
fun copyOrMoveDir(srcDirPath: String, destDirPath: String, isMove: Boolean = false): Boolean {
    val srcDirFile = getFileByPath(srcDirPath)
    val destDirFile = getFileByPath(destDirPath)
    return if (srcDirFile != null && destDirFile != null) {
        srcDirFile.copyOrMoveDir(destDirFile, isMove)
    } else {
        false
    }
}

/**
 * Copy or move files (default is copy files)
 *
 * @param destFile
 * @param isMove default false
 */
fun File.copyOrMoveFile(destFile: File, isMove: Boolean = false): Boolean {
    if (!exists() || !isFile) return false
    if (!destFile.exists() || !destFile.isFile) return false

    if (parentFile?.createOrExistsDir() != true) return false

    return try {
        destFile.writeISAsFile(FileInputStream(this)) && !(isMove && !deleteFile())
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
        false
    }
}

/**
 * Copy or move files (default is copy files)
 *
 * @param srcPath
 * @param destPath
 * @param isMove default false
 */
fun copyOrMoveFile(srcPath: String, destPath: String, isMove: Boolean = false): Boolean {
    val srcFile = getFileByPath(srcPath)
    val destFile = getFileByPath(destPath)
    return if (srcFile != null && destFile != null) {
        srcFile.copyOrMoveFile(destFile, isMove)
    } else {
        false
    }
}


/**
 * Delete folder
 *
 * @return true or false
 *
 */
fun File.deleteDir(): Boolean {
    if (!exists()) return true
    if (!isDirectory) return false

    val files = listFiles()
    files?.forEach {
        if (it.isFile) {
            if (!it.delete()) return false
        } else if (it.isDirectory) {
            if (!deleteDir()) return false
        }
    }

    return delete()
}

/**
 * Delete folder
 *
 * @param dirPath
 * @return true or false
 *
 */
fun deleteDir(dirPath: String): Boolean {
    val file = getFileByPath(dirPath)
    return file?.deleteDir() ?: false
}

/**
 * Delete Files
 *
 * @return true or false
 */
fun File.deleteFile(): Boolean = !exists() || (isFile && delete())

/**
 * Delete Files
 *
 * @param filePath
 * @return true or false
 */
fun deleteFile(filePath: String): Boolean {
    val file = getFileByPath(filePath)
    return file?.deleteFile() ?: false
}