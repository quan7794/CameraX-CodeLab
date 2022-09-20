package com.samsung.android.architecture.library.pickimage.data

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.samsung.android.architecture.library.pickimage.util.createCursor
import com.samsung.android.architecture.library.pickimage.util.fetchGalleryAlbums
import com.samsung.android.architecture.library.pickimage.util.fetchPagePicture
import kotlinx.coroutines.flow.Flow

private const val Zero = 0
private const val One = 1

interface GalleryRepository {
    suspend fun getCount(): Int
    suspend fun getByOffset(offset: Int): GalleryData?
    suspend fun getPhoneAlbums(): GalleryModel
    fun getAllPhoto(): Flow<PagingData<GalleryData>>
}

class GalleryRepositoryImpl(private val context: Context) : GalleryRepository {

    override suspend fun getCount(): Int {
        val cursor = context.createCursor(Int.MAX_VALUE, Zero) ?: return Zero
        val count = cursor.count
        cursor.close()
        return count
    }

    override suspend fun getByOffset(offset: Int): GalleryData? {
        return context.fetchPagePicture(One, offset).firstOrNull()
    }

    override fun getAllPhoto(): Flow<PagingData<GalleryData>> = Pager(
        config = PagingConfig(pageSize = 50, initialLoadSize = 50, enablePlaceholders = true)
    ) {
        GalleryDataSource { limit, offset -> context.fetchPagePicture(limit, offset) }
    }.flow

    override suspend fun getPhoneAlbums(): GalleryModel {
       return context.fetchGalleryAlbums()
    }
}
