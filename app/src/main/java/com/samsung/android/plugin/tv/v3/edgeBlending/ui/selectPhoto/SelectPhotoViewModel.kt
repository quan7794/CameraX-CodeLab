package com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto

import android.content.Context
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.library.pickimage.GalleryConfiguration
import com.samsung.android.architecture.library.pickimage.data.GalleryAlbums
import com.samsung.android.architecture.library.pickimage.data.GalleryRepository
import com.samsung.android.architecture.library.pickimage.util.GalleryUriManager
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.model.PhotoModel
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.viewmodel.NavigateUpViewModel
import kotlinx.coroutines.*
import kotlin.collections.ArrayList

class SelectPhotoViewModel(
    private val pickImageRepository: GalleryRepository
) : NavigateUpViewModel() {
    private var selectedImageList: List<PhotoModel.Data> = ArrayList()
    lateinit var allPhotos: ArrayList<PhotoModel.Data>
    private val albumList: MutableList<GalleryAlbums> = ArrayList()
    private val albumNames: MutableList<String> = ArrayList()
    private var preSelectedState = PreSelectedState()

    fun getPrevAlbumAt(): Int {
        return preSelectedState.prevAlbumAt
    }

    fun selectedPhoto() {
        selectedImageList = allPhotos.filter { it.isSelected }
        _state.value = SelectPhotoState.SelectedPhoto(selectedImageList)
    }

    fun getAlbums(context: Context) {
        uiScope.launch(handlerCoroutineException) {
            withContext(Dispatchers.Default) {
                val galleryModel = pickImageRepository.getPhoneAlbums()
                galleryModel.albums.sortWith(compareBy { it.name })
                for (album in galleryModel.albums) {
                    albumNames.add("${album.name} (${album.albumPhotos.size})")
                    albumList.add(album)
                }
                val photos = galleryModel.photos
                val viewAllString =
                    context.getString(R.string.COM_TV_SID_HOTEL_VIEW_ALL_LOWER) + " (${photos.size})"
                albumNames.add(0, viewAllString)
                albumList.add(0, GalleryAlbums(0, viewAllString, albumPhotos = photos))

            }
            allPhotos = getAllPhoto()
            _state.value = SelectPhotoState.GetAlbumNames(albumNames)

            progressLiveEvent.value = false

        }
    }

    private fun getAllPhoto(): ArrayList<PhotoModel.Data> {
        val photos = ArrayList<PhotoModel.Data>()
        albumList[0].albumPhotos.forEach { galleryData ->
            var isSelected = false
            var isEnable = true
            var numSelected = 0
            if (selectedImageList.isNotEmpty()) {
                selectedImageList.forEach {
                    if (galleryData.id == it.id) {
                        isEnable = true
                        isSelected = true
                        numSelected = it.numSelected } }
            }

            photos.add(PhotoModel.Data(
                    galleryData?.id,
                    galleryData.albumId,
                    galleryData.uri,
                    isSelected,
                    albumList[0].name, isEnabled = isEnable, numSelected = numSelected))
        }

        return photos
    }

    fun getAlbum(position: Int) {
        val photos = if (position == 0) allPhotos else { allPhotos.filter { it.albumId == albumList[position].id } }
        _state.value = SelectPhotoState.GetAlbum(albumNames[position], photos)
    }

    fun onNextClick() {
        _clickEvent.value = SelectPhotoState.GotoCropPhotoScreen(ArrayList(selectedImageList.map { it.uri }))
    }

    fun onAlbumClick() {
        _clickEvent.value = SelectPhotoState.AlbumClick
    }

    override fun onBackClick() {
        super.onBackClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        albumNames.clear()
        albumList.clear()
    }

}
