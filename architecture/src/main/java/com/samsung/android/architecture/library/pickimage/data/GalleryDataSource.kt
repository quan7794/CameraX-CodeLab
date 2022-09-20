package com.samsung.android.architecture.library.pickimage.data

import androidx.paging.PagingSource
import androidx.paging.PagingState

private const val Zero = 0
private const val One = 1

class GalleryDataSource(private val onFetch: (limit: Int, offset: Int) -> List<GalleryData>) :
    PagingSource<Int, GalleryData>() {

    override fun getRefreshKey(state: PagingState<Int, GalleryData>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(One)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(One)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryData> {
        val pageNumber = params.key ?: Zero
        val pageSize = params.loadSize
        val pictures = onFetch.invoke(pageSize, pageNumber * pageSize)
        val prevKey = if (pageNumber > Zero) pageNumber.minus(One) else null
        val nextKey = if (pictures.isNotEmpty()) pageNumber.plus(One) else null

        return LoadResult.Page(
            data = pictures,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }
}
