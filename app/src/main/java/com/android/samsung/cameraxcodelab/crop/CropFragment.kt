package com.android.samsung.cameraxcodelab.crop

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.android.samsung.cameraxcodelab.R
import com.android.samsung.cameraxcodelab.databinding.FragmentCropBinding
import com.jintin.bindingextension.BindingFragment

class CropFragment : BindingFragment<FragmentCropBinding>() {

    private lateinit var viewModel: CropViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CropViewModel::class.java]
        binding.textView.text =" Loaded"
        binding.cropView.apply {
            setImageDrawable(resources.getDrawable(R.drawable.ic_launcher_background),0.4F,10F)
            setAspectRatio(21,9)
        }
    }

    companion object {
        fun newInstance() = CropFragment()
    }
}