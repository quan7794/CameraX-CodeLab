package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.*
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.CropPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.model.ViewState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.view.ImageCropView
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.cropImageView.view.util.dpToPx
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util.Utils
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto.util.enable
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
    private val viewStateList = mutableMapOf<Int, ViewState>()
    private val stateArrayList = mutableMapOf<Int, FloatArray>()
    private var imageIndex = 0
    override val viewModel: CropPhotoVM by lazy { getNormalViewModel(CropPhotoVM()) }
    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        initCropView(viewModel.uriList, imageIndex)
        binding.btnCrop.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
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
        }
        binding.btnReset.setOnClickListener { binding.cropView.resetMatrix() }
        binding.btnNextImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(btnNext: View) {
                stateArrayList[imageIndex] = binding.cropView.getPositionInfo()
                imageIndex++
                restoreCropViewState()
                if (imageIndex >= viewModel.uriList.size - 1) btnNext.enable(false)
                binding.btnBackImage.enable(true)
            }
        })
        binding.btnBackImage.setOnClickListener(object : View.OnClickListener {
            override fun onClick(btnBack: View) {
                binding.btnNextImage.enable(true)
                stateArrayList[imageIndex] = binding.cropView.getPositionInfo()
                imageIndex--
                restoreCropViewState()
                if (imageIndex == 0) btnBack.enable(false)
            }
        })
    }

    private fun restoreCropViewState() {
        binding.cropView.setImageUri(viewModel.uriList[imageIndex])
        stateArrayList[imageIndex]?.let { binding.cropView.setPositionInfo(it) }
    }

    private fun initCropView(uri: List<Uri>, index: Int) {
        Log.d("initCropView", "Entry")
        binding.cropView.apply {
            binding.cropView.setImageUri(uri[index])
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
        binding.btnBackImage.enable(false)
    }

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        val uriList = arguments?.getParcelableArrayList<Uri>(SelectPhotoFragment.KEY_URI)
        uriList?.let {
            viewModel.uriList = uriList
        }
    }

    companion object {
        val TAG: String by getTagName()

        fun newInstance(uri: ArrayList<Uri>) = CropPhotoFragment().putArgs {
            putParcelableArrayList(SelectPhotoFragment.KEY_URI, uri)
        }
    }
}