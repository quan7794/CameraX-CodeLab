package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.confirm

import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigConnectDeviceFragmentBinding

class ConfigConfirmFragment:BaseVmDbFragment<ConfigConnectDeviceFragmentBinding, ConfigConfirmViewModel>() {

    override fun getLayoutId(): Int = R.layout.config_confirm_fragment

    override val viewModel: ConfigConfirmViewModel by lazy { getNormalViewModel(ConfigConfirmViewModel()) }

}