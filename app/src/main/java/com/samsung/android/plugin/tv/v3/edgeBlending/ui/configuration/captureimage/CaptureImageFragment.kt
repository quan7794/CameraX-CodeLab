package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.captureimage

import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigCaptureImageFragmentBinding

class CaptureImageFragment: BaseVmDbFragment<ConfigCaptureImageFragmentBinding, CaptureImageViewModel>() {

    override fun getLayoutId(): Int  = R.layout.config_capture_image_fragment

    override val viewModel: CaptureImageViewModel by lazy { CaptureImageViewModel() }

}