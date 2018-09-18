package com.fanhl.kona.util

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.os.Environment
import android.util.Log
import com.fanhl.kona.BuildConfig
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileOutputStream


object FileUtils {
    private val TAG = FileUtils::class.java.simpleName
    private const val PHOTOS = "photos"

    private fun getSavePath() = if (hasSDCard()) { // SD card
        File(getSDCardPath() + "/${BuildConfig.APPLICATION_ID}/").apply {
            if (!exists()) {
                mkdirs()
            }
        }
    } else {
        Environment.getDataDirectory()
    }

    private fun getPhotoSavePath() = File(getSavePath(), PHOTOS).apply {
        if (!exists()) {
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

    private fun saveBitmap(filename: String, bmp: Bitmap) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(filename)
            bmp.compress(CompressFormat.PNG, 100, out)
        } catch (e: Exception) {
            Log.e(TAG, "saveBitmap: ", e)
        } finally {
            try {
                out?.flush()
                out?.close()
            } catch (e: Exception) {
            }
        }
    }

    fun savePhoto(photoId: Long, bitmap: Bitmap) {
        val file = File(getPhotoSavePath(), "$photoId.jpg")
        if (file.exists()) {
            return
        }
        doAsync {
            saveBitmap(file.absolutePath, bitmap)
        }
    }
}