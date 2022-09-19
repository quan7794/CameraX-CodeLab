package com.android.samsung.cameraxcodelab.crop

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.samsung.cameraxcodelab.crop.imageCropView.view.ImageCropView
import com.android.samsung.cameraxcodelab.crop.imageCropView.view.util.dpToPx
import com.android.samsung.cameraxcodelab.databinding.FragmentCropBinding
import com.jintin.bindingextension.BindingFragment
import java.io.File

class CropFragment : BindingFragment<FragmentCropBinding>() {

    private lateinit var viewModel: CropViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CropViewModel::class.java]
        initCropView()
        binding.resetButton.setOnClickListener {
            val cropImage = binding.cropView.croppedImage
            val saveDir = File(requireContext().cacheDir, "EbCache").also { if (!it.exists()) it.mkdir() }
            val result = Utils.saveImage(cropImage, saveDir, "aaaaa.png")
            if (result) Toast.makeText(requireContext(), "Save done at: $saveDir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCropView() {
        binding.cropView.apply {
            setImageFilePath("${requireContext().cacheDir}/EbImage/image1.jpg")
            setAspectRatio(21, 9)
            setGridLeftRightMargin(20)
            setGridTopBottomMargin(20)
            initRelateView(22, 24)
        }
    }

    private fun ImageCropView.initRelateView(topMarginDp: Int, bottomMarginDp: Int) {
        waitForLayout {
            binding.resetButton.setMargin(-(this.bottom - (mCropRect.bottom + dpToPx(bottomMarginDp))).toInt())
            binding.descriptionText.setMargin(0,-(mCropRect.top - dpToPx(topMarginDp) - this.top).toInt())
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        initCropView()
    }
    companion object {
        val TAG: String = CropFragment::class.java.simpleName
        fun newInstance() = CropFragment()
    }
}