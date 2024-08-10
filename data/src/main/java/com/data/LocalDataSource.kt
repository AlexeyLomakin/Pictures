package com.data

import android.content.Context
import java.io.File
import javax.inject.Inject

class LocalDataSource @Inject constructor(private val context: Context) {

    fun getOrCreateLocalDirectory(): File {
        val directory = File(context.getExternalFilesDir(null), "saved_images")
        return directory
    }

    fun getLocalDirectoryPath(): String? {
        val directory = getOrCreateLocalDirectory()
        return directory.absolutePath
    }
}
