package com.android.samsung.cameraxcodelab.crop

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.android.samsung.cameraxcodelab.databinding.FragmentCropBinding
import com.jintin.bindingextension.BindingFragment
import java.io.File

class CropFragment : BindingFragment<FragmentCropBinding>() {

    private lateinit var viewModel: CropViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CropViewModel::class.java]
        binding.cropView.apply {
            setImageFilePath("${requireContext().cacheDir}/EbImage/image.jpg")
            setAspectRatio(21,9)
            setGridLeftRightMargin(20)
            setGridTopBottomMargin(20)
        }
        binding.Reset.setOnClickListener {
            val cropImage = binding.cropView.croppedImage
            val saveDir = File(requireContext().cacheDir,"EbCache").also { if (!it.exists()) it.mkdir() }
            val result = Utils.saveImage(cropImage, saveDir, "aaaaa.png")
            if (result) Toast.makeText(requireContext(), "Save done at: $saveDir", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = CropFragment()
    }
}