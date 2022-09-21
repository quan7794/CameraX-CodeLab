package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.*
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.CropPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.adapter.SlideShowPhotoAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.view.ImageCropView
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.view.util.dpToPx
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util.Utils
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util.setMargin
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util.waitForLayout
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class CropPhotoFragment : BaseVmDbFragment<CropPhotoFragmentBinding, CropPhotoVM>() {
    override fun getLayoutId(): Int = R.layout.crop_photo_fragment
    override val viewModel: CropPhotoVM by lazy { getNormalViewModel(CropPhotoVM()) }
    private lateinit var slideShowAdapter: SlideShowPhotoAdapter

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        initCropView(viewModel.uriList)
        initSlideShowArea()
    }

    private fun initSlideShowArea() {
        slideShowAdapter = SlideShowPhotoAdapter(viewModel.uriList, onSlideShowItemClick)
        binding.selectedPhotos.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = slideShowAdapter
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private val onSlideShowItemClick: (Int, Uri) -> Unit = { clickPosition, _ ->
        slideShowAdapter.apply {
            selectedPosition = clickPosition
            notifyDataSetChanged()
            updateCropView(clickPosition)
        }
    }

    private fun updateCropView(newPosition: Int) {
        viewModel.viewStateList[slideShowAdapter.lastSelectedPosition] = binding.cropView.getPositionInfo()
        slideShowAdapter.lastSelectedPosition = newPosition
        updateCropViewState(newPosition)
    }

    private fun updateCropViewState(pos: Int) {
        binding.cropView.setImageUri(viewModel.uriList[pos])
        viewModel.viewStateList[pos]?.let { binding.cropView.setPositionInfo(it) }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.state) {
            when (it) {
                is CropPhotoState.CropState -> cropImage()
            }
        }
    }

    private fun cropImage() = CoroutineScope(Dispatchers.IO).launch {
        withContext(Dispatchers.Main) { binding.btnCrop.text = "Saving" }
        val cropImage = binding.cropView.croppedImage
        val saveDir = File(requireContext().cacheDir, "EbCache").also { if (!it.exists()) it.mkdir() }
        val result = Utils.saveImage(cropImage, saveDir, "aaaaa")
        withContext(Dispatchers.Main) {
            if (result) {
                binding.btnCrop.text = "Crop & Save"
                Toast.makeText(requireContext(), "Save done at: $saveDir", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initCropView(uri: List<Uri>) {
        Log.d("initCropView", "Entry")
        binding.cropView.apply {
            binding.cropView.setImageUri(uri[0])
            setAspectRatio(21, 9)
            setGridMargin(0, 20)
            initRelateView(22, 24)
        }
    }

    private fun ImageCropView.initRelateView(topMarginDp: Int, bottomMarginDp: Int) {
        waitForLayout {
            binding.functionArea.setMargin(-(this.bottom - (mCropRect.bottom + dpToPx(bottomMarginDp))).toInt())
            binding.descriptionText.setMargin(0, -(mCropRect.top - dpToPx(topMarginDp) - this.top).toInt())
        }
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
        binding.viewModel = viewModel
    }

    companion object {
        val TAG: String by getTagName()

        fun newInstance(uri: ArrayList<Uri>) = CropPhotoFragment().putArgs {
            putParcelableArrayList(SelectPhotoFragment.KEY_URI, uri)
        }
    }
}