package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util

import android.annotation.SuppressLint
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.lang.Exception
import java.lang.NumberFormatException
import java.util.*
import kotlin.math.min

object FileUtils {
    private val TAG = FileUtils::class.java.simpleName

    fun getPath(context: Context, uri: Uri): String? {
        // check here to KITKAT or new version
        val selection: String
        val selectionArgs: Array<String>
        // DocumentProvider
        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            val fullPath = getPathFromExtSD(split)
            return if (fullPath != "") {
                fullPath
            } else {
                null
            }
        }

        // DownloadsProvider
        if (isDownloadsDocument(uri)) {
            context.contentResolver.query(uri, arrayOf(MediaStore.MediaColumns.DISPLAY_NAME), null, null, null).use { cursor ->
                if (cursor != null && cursor.moveToFirst()) {
                    val fileName = cursor.getString(0)
                    val path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName
                    if (!TextUtils.isEmpty(path)) {
                        return path
                    }
                }
            }
            val id: String = DocumentsContract.getDocumentId(uri)
            if (!TextUtils.isEmpty(id)) {
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:".toRegex(), "")
                }
                val contentUriPrefixesToTry = arrayOf("content://downloads/public_downloads", "content://downloads/my_downloads")
                for (contentUriPrefix in contentUriPrefixesToTry) {
                    return try {
                        val contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), id.toLong())
                        getDataColumn(context, contentUri, null, null)
                    } catch (e: NumberFormatException) {
                        //In Android 8 and Android P the id is not a number
                        uri.path!!.replaceFirst("^/document/raw:".toRegex(), "").replaceFirst("^raw:".toRegex(), "")
                    }
                }
            }
        }

        // MediaProvider
        if (isMediaDocument(uri)) {
            val docId = DocumentsContract.getDocumentId(uri)
            val split = docId.split(":".toRegex()).toTypedArray()
            val type = split[0]
            var contentUri: Uri? = null
            when (type) {
                "image" -> contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                "video" -> contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                "audio" -> contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
            selection = "_id=?"
            selectionArgs = arrayOf(split[1])
            return getDataColumn(context, contentUri, selection, selectionArgs)
        }
        if (isGoogleDriveUri(uri)) return getDriveFilePath(context, uri)
        if (isWhatsAppFile(uri)) return getFilePathForWhatsApp(context, uri)
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            if (isGooglePhotosUri(uri)) return uri.lastPathSegment
            if (isGoogleDriveUri(uri)) return getDriveFilePath(context, uri)
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // return getFilePathFromURI(context,uri);
                copyFileToInternalStorage(context, uri, "userfiles")
                // return getRealPathFromURI(context,uri);
            } else {
                getDataColumn(context, uri, null, null)
            }
        }
        return if ("file".equals(uri.scheme, ignoreCase = true)) uri.path else null
    }

    private fun fileExists(filePath: String): Boolean {
        val file = File(filePath)
        return file.exists()
    }

    private fun getPathFromExtSD(pathData: Array<String>): String {
        val type = pathData[0]
        val relativePath = "/" + pathData[1]
        var fullPath: String

        // on Sony devices (4.4.4 & 5.1.1), `type` is a dynamic string
        // something like "71F8-2C0A", some kind of unique id per storage
        // don't know any API that can get the root path of that storage based on its id.
        //
        // so no "primary" type, but let the check here for other devices
        if ("primary".equals(type, ignoreCase = true)) {
            fullPath = Environment.getExternalStorageDirectory().toString() + relativePath
            if (fileExists(fullPath)) {
                return fullPath
            }
        }

        // Environment.isExternalStorageRemovable() is `true` for external and internal storage
        // so we cannot relay on it.
        //
        // instead, for each possible path, check if file exists
        // we'll start with secondary storage as this could be our (physically) removable sd card
        fullPath = System.getenv("SECONDARY_STORAGE") + relativePath
        if (fileExists(fullPath)) return fullPath
        fullPath = System.getenv("EXTERNAL_STORAGE") + relativePath
        return if (fileExists(fullPath)) fullPath else fullPath
    }

    private fun getDriveFilePath(context: Context, uri: Uri): String {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        val returnCursor = context.contentResolver.query(uri, null, null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val file = File(context.cacheDir, name)
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            outputStream = FileOutputStream(file)
            var read: Int
            val maxBufferSize = 1024 * 1024
            val bytesAvailable = inputStream!!.available()

            //int bufferSize = 1024;
            val bufferSize = min(bytesAvailable, maxBufferSize)
            val buffers = ByteArray(bufferSize)
            while (inputStream.read(buffers).also { read = it } != -1) {
                outputStream.write(buffers, 0, read)
            }
            Log.e("File Size", "Size " + file.length())
            Log.e("File Path", "Path " + file.path)
            Log.e("File Size", "Size " + file.length())
        } catch (e: Exception) {
            Log.e(TAG, "Exception" + Objects.requireNonNull(e.message))
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
                returnCursor.close()
            } catch (e: Exception) {
                Log.e(TAG, "Exception" + Objects.requireNonNull(e.message))
            }
        }
        return file.path
    }

    /***
     * Used for Android Q+
     * @param uri
     * @param newDirName if you want to create a directory, you can set this variable
     * @return
     */
    private fun copyFileToInternalStorage(context: Context, uri: Uri, newDirName: String): String {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        val returnCursor = context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE), null, null, null)
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
        returnCursor.moveToFirst()
        val name = returnCursor.getString(nameIndex)
        val size = returnCursor.getLong(sizeIndex).toString()
        val output: File = if (newDirName != "") {
            val dir = File(context.filesDir.toString() + "/" + newDirName)
            if (!dir.exists()) dir.mkdir()
            File(context.filesDir.toString() + "/" + newDirName + "/" + name)
        } else File(context.filesDir.toString() + "/" + name)
        try {
            inputStream = context.contentResolver.openInputStream(uri)
            outputStream = FileOutputStream(output)
            var read: Int
            val bufferSize = 1024
            val buffers = ByteArray(bufferSize)
            while (inputStream!!.read(buffers).also { read = it } != -1) outputStream.write(buffers, 0, read)
        } catch (e: Exception) {
            returnCursor.close()
            Log.e(TAG, "Exception" + Objects.requireNonNull(e.message))
            return ""
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
                returnCursor.close()
            } catch (e: Exception) {
                Log.e(TAG, "Exception" + Objects.requireNonNull(e.message))
            }
        }
        return output.path
    }

    private fun getFilePathForWhatsApp(context: Context, uri: Uri) = copyFileToInternalStorage(context, uri, "whatsapp")

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri) = "com.android.externalstorage.documents" == uri.authority
    private fun isDownloadsDocument(uri: Uri) = "com.android.providers.downloads.documents" == uri.authority
    private fun isMediaDocument(uri: Uri) = "com.android.providers.media.documents" == uri.authority
    private fun isGooglePhotosUri(uri: Uri) = "com.google.android.apps.photos.content" == uri.authority
    fun isWhatsAppFile(uri: Uri) = "com.whatsapp.provider.media" == uri.authority
    private fun isGoogleDriveUri(uri: Uri) = "com.google.android.apps.docs.storage" == uri.authority || "com.google.android.apps.docs.storage.legacy" == uri.authority
}