package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.confirm

import android.os.Bundle
import androidx.fragment.app.setFragmentResultListener
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.logD
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigConnectDeviceFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants

class ConfigConfirmFragment:BaseVmDbFragment<ConfigConnectDeviceFragmentBinding, ConfigConfirmViewModel>() {

    override fun getLayoutId(): Int = R.layout.config_confirm_fragment

    override val viewModel: ConfigConfirmViewModel by lazy { getNormalViewModel(ConfigConfirmViewModel()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(ConfigConstants.REQUEST_CONFIRM_BUTTON_CLICK){ _, _->
            logD("CONFIRM_BUTTON_CLICK")
        }
    }
}