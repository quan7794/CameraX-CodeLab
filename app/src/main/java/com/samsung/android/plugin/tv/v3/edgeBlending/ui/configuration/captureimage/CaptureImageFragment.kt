package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.captureimage

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.logD
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigCaptureImageFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants

class CaptureImageFragment: BaseVmDbFragment<ConfigCaptureImageFragmentBinding, CaptureImageViewModel>() {

    override fun getLayoutId(): Int  = R.layout.config_capture_image_fragment

    override val viewModel: CaptureImageViewModel by lazy { CaptureImageViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ConfigConstants.REQUEST_BLENDING_BUTTON_CLICK){_,_->
           logD("BLENDING_BUTTON_CLICK")
        }
    }

}