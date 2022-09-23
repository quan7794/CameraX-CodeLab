package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.observe
import com.samsung.android.architecture.ext.putArgs
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.EbEditPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.adapter.SelectedPhotosAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util.dpToPx
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.view.ImageCropView
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.EditPhotoState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.ViewOnTvState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.*


class EditPhotoFragment : BaseVmDbFragment<EbEditPhotoFragmentBinding, EditPhotoViewModel>() {
    override fun getLayoutId(): Int = R.layout.eb_edit_photo_fragment
    override val viewModel: EditPhotoViewModel by lazy { getNormalViewModel(EditPhotoViewModel()) }
    private lateinit var slideShowAdapter: SelectedPhotosAdapter

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        initCropView(viewModel.uriList)
        initSlideShowArea()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSlideShowArea() {
        slideShowAdapter = SelectedPhotosAdapter(viewModel.uriList, onSlideShowItemClick)
        binding.selectedPhotos.adapter = slideShowAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private val onSlideShowItemClick: (Int, Uri) -> Unit = { clickPosition, _ ->
        slideShowAdapter.apply {
            currentPosition = clickPosition
            notifyDataSetChanged()
            updateCropView(clickPosition)
            binding.selectedPhotos.smoothScrollToPosition(clickPosition)
        }
    }

    private fun updateCropView(newPosition: Int) {
        viewModel.cropStateList[slideShowAdapter.recentPosition] = binding.cropView.getPositionInfo()
        slideShowAdapter.recentPosition = newPosition
        updateCropViewState(newPosition)
    }

    private fun updateCropViewState(position: Int) = binding.cropView.apply {
        setImageUri(viewModel.uriList[position])
        viewModel.cropStateList[position]?.let { setPositionInfo(it) } ?: run { resetDisplay() }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.clickEvent) {
            when (it) {
                is EditPhotoState.ClickCropPhoto -> lifecycleScope.launch(Dispatchers.IO) { cropImage() }
                is EditPhotoState.ClickViewOnTv -> viewOnTv()
            }
        }
        observe(viewModel.state) {
            binding.btnViewOnTv.disableWhenViewOnTv(it)
            when (it) {
                is ViewOnTvState.Running -> {
                    Log.d("ViewOnTvState", "Running")
                    binding.btnViewOnTv.enable(false)
                }
                is ViewOnTvState.Success -> {
                    Log.d("ViewOnTvState", "Success")
                }
                is ViewOnTvState.Cancel -> {
                    Log.d("ViewOnTvState", "Cancel")
                }
                is ViewOnTvState.Error -> {
                    Log.d("ViewOnTvState", "Error: ${it.cause}")
                }
            }
        }
    }

    private fun viewOnTv() {
        viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                updateViewOnTvState(ViewOnTvState.Running)
                viewModel.uriList.forEachIndexed { index, uri ->
                    withContext(Dispatchers.Main) { onSlideShowItemClick(index, uri) }
                    delay(200)
                    cropImage(index.toString())
                }
                updateViewOnTvState(ViewOnTvState.Success)
            }.getOrElse { updateViewOnTvState(ViewOnTvState.Error("Message: ${it.message}")) }
        }
    }

    private fun updateViewOnTvState(status: ViewOnTvState) {
        viewModel.updateUiState(status)
    }

    private suspend fun cropImage(fileName: String = "temp") = withContext(Dispatchers.IO) {
        Log.e("cropImage", "Status: Entry_______")
        val saveDir = File(requireContext().cacheDir, "EbCache").also { if (!it.exists()) it.mkdir() }
        Log.e("cropImage", "$fileName.jpg")
        val cropImage = binding.cropView.getCroppedImage()
        val result = Utils.saveImage(cropImage, saveDir, fileName)
        Log.e("cropImage", "Status: $result")
    }

    private fun initCropView(uri: List<Uri>) {
        Log.d("initCropView", "Entry")
        binding.cropView.apply {
            binding.cropView.setImageUri(uri[0])
            setAspectRatio(21, 9)
            setCropViewMargin(horizontal = HORIZONTAL_CROP_VIEW_MARGIN)
            initConstraintWithCropView(22, 24)
        }
    }

    private fun ImageCropView.initConstraintWithCropView(topMarginDp: Int, bottomMarginDp: Int) = waitForLayout {
        val bottomMargin = -(mCropRect.top - dpToPx(topMarginDp) - this.top).toInt()
        val topMargin = -(this.bottom - (mCropRect.bottom + dpToPx(bottomMarginDp))).toInt()
        binding.functionArea.setMargin(top = topMargin)
        binding.descriptionText.setMargin(bottom = bottomMargin)
    }

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        val selectedPhotos = arguments?.getParcelableArrayList<Uri>(SelectPhotoFragment.KEY_URI)
        selectedPhotos?.let {
            viewModel.apply {
                uriList = selectedPhotos
                setSlideVisibility(uriList.size > 1)
            }
        }
    }

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.vm = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.state.value == ViewOnTvState.Running) updateViewOnTvState(ViewOnTvState.Error("User turn off screen."))
    }

    companion object {
        val TAG: String by getTagName()
        const val HORIZONTAL_CROP_VIEW_MARGIN = 20
        fun newInstance(uri: ArrayList<Uri>) = EditPhotoFragment().putArgs {
            putParcelableArrayList(SelectPhotoFragment.KEY_URI, uri)
        }
    }
}