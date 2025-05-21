package com.fanhl.kona.common.util

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    fun downloadFile(url: String, fileName: String): Long {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle(fileName)
            .setDescription("Downloading $fileName")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, fileName)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

        return downloadManager.enqueue(request)
    }
} 