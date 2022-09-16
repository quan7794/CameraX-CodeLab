package com.android.samsung.cameraxcodelab.crop

import android.graphics.Bitmap
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class Utils {
    companion object {
        fun saveImage(bitmap: Bitmap?, directory: File, fileName: String): Boolean {
            bitmap ?: return false
            val file = File(directory, fileName)
            Log.d("createFile", "file out:$directory/$fileName")
            if (file.exists()) {
                file.delete()
            }
            var fileStream: FileOutputStream? = null
            return try {
                fileStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileStream)
            } catch (e: FileNotFoundException) {
                Log.e("createFile", "FileNotFoundException is occurred : " + e.message)
                false
            } finally {
                if (fileStream != null) {
                    try {
                        fileStream.close()
                    } catch (e: IOException) {
                        Log.e("createFile", "Close stream exception is occurred : " + e.message)
                    }
                }
            }
        }
    }
}