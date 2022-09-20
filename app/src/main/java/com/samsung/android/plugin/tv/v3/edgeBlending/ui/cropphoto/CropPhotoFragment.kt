package com.samsung.android.plugin.tv.v3.edgeBlending.ui.cropphoto

import android.net.Uri
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.putArgs
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.CropPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment


class CropPhotoFragment: BaseVmDbFragment<CropPhotoFragmentBinding, CropPhotoVM>() {
    override fun getLayoutId(): Int = R.layout.crop_photo_fragment

    override val viewModel: CropPhotoVM by lazy { getNormalViewModel(CropPhotoVM()) }
    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        val uri = arguments?.getParcelableArrayList<Uri>(SelectPhotoFragment.KEY_URI)
        viewModel.setUri(uri?.get(0))

    }

    companion object {
        val TAG: String by getTagName()

        fun newInstance(uri: ArrayList<Uri>) = CropPhotoFragment().putArgs {
            putParcelableArrayList(SelectPhotoFragment.KEY_URI,uri)
        }
    }
}