package com.samsung.android.architecture.library.pickimage.util

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import androidx.core.os.bundleOf
import com.samsung.android.architecture.library.pickimage.data.GalleryAlbums
import com.samsung.android.architecture.library.pickimage.data.GalleryData
import com.samsung.android.architecture.library.pickimage.data.GalleryModel

private val projection = arrayOf(
    MediaStore.Images.Media._ID,
    MediaStore.Images.Media.DISPLAY_NAME,
    MediaStore.Images.Media.DATE_TAKEN,
    MediaStore.Images.Media.BUCKET_DISPLAY_NAME
)

internal fun Context.createCursor(limit: Int = 0, offset: Int = 0): Cursor? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val bundle = if (offset == 0) {
            bundleOf(
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_ADDED),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        } else {
            bundleOf(
            ContentResolver.QUERY_ARG_OFFSET to offset,
            ContentResolver.QUERY_ARG_LIMIT to limit,
            ContentResolver.QUERY_ARG_SORT_COLUMNS to arrayOf(MediaStore.Images.Media.DATE_ADDED),
            ContentResolver.QUERY_ARG_SORT_DIRECTION to ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
        }

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            bundle,
            null
        )
    } else {
        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $limit OFFSET $offset",
            null
        )
    }
}

internal fun Context.fetchPagePicture(limit: Int, offset: Int): List<GalleryData> {
    val pictures = ArrayList<GalleryData>()
    val cursor = createCursor(limit, offset)
    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(projection[0])
        val displayNameColumn = it.getColumnIndexOrThrow(projection[1])
        val dateTakenColumn = it.getColumnIndexOrThrow(projection[2])
        val bucketDisplayName = it.getColumnIndexOrThrow(projection[3])

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val dateTaken = it.getLong(dateTakenColumn)
            val displayName = it.getString(displayNameColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            val folderName = it.getString(bucketDisplayName)

            pictures.add(
                GalleryData(
                    contentUri,
                    dateTaken,
                    displayName,
                    id,
                    folderName.toString()
                )
            )
        }
    }
    cursor?.close()
    return pictures
}

internal fun Context.fetchGalleryAlbums(): GalleryModel {

    val galleryAlbums: ArrayList<GalleryAlbums> = ArrayList()
    val albumsNames: ArrayList<String?> = ArrayList()
    val pictures = ArrayList<GalleryData>()
    val cursor = createCursor()
    cursor?.use {
        val idColumn = it.getColumnIndexOrThrow(projection[0])
        val displayNameColumn = it.getColumnIndexOrThrow(projection[1])
        val dateTakenColumn = it.getColumnIndexOrThrow(projection[2])
        val bucketDisplayName = it.getColumnIndexOrThrow(projection[3])

        while (it.moveToNext()) {
            val id = it.getLong(idColumn)
            val dateTaken = it.getLong(dateTakenColumn)
            val displayName = it.getString(displayNameColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                id
            )
            val folderName = it.getString(bucketDisplayName)
            val pickImageData = GalleryData(
                contentUri,
                dateTaken,
                displayName,
                id,
                folderName.toString()
            )
            if (albumsNames.contains(pickImageData.folderName)) {
                for (album in galleryAlbums) {
                    if (album.name == pickImageData.folderName) {
                        pickImageData.albumId = album.id
                        album.albumPhotos.add(pickImageData)
                         pictures.add(pickImageData)
                        break
                    }
                }
            } else {
                val album = GalleryAlbums(pickImageData.id
                    ,pickImageData.folderName
                ,pickImageData.uri).apply {
                    albumPhotos.add(pickImageData)
                }
                pickImageData.albumId = pickImageData.id
                pictures.add(pickImageData)
                albumsNames.add(album.name)
                galleryAlbums.add(album)
            }


        }
    }
    cursor?.close()
    return GalleryModel(pictures, galleryAlbums)
}

