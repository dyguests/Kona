package com.fanhl.kona.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import com.fanhl.kona.BuildConfig
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    private const val PHOTOS = "photos"

    fun getSavePath(): File {
        val path: File
        if (hasSDCard()) { // SD card
            path = File(getSDCardPath() + "/${BuildConfig.APPLICATION_ID}/")
            path.mkdirs()
        } else {
            path = Environment.getDataDirectory()
        }
        return path
    }

    fun getPhotoSavePath(): File {
        return File(getSavePath(), PHOTOS).apply {
            mkdirs()
        }
    }

    private fun hasSDCard(): Boolean { // SD????????
        val status = Environment.getExternalStorageState()
        return status == Environment.MEDIA_MOUNTED
    }

    private fun getSDCardPath(): String {
        val path = Environment.getExternalStorageDirectory()
        return path.absolutePath
    }

    fun saveToFile(filename: String, bmp: Bitmap) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filename)
            bmp.compress(CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
        } finally {
            try {
                out?.flush()
                out?.close()
            } catch (e: Exception) {
            }
        }
    }

    fun save(photoId: Long, bitmap: Bitmap) {
        val file = File(getPhotoSavePath(), "$photoId.jpg")
        if (file.exists()) {
            return
        }
        doAsync {
            saveToFile(file.absolutePath, bitmap)
        }
    }
}