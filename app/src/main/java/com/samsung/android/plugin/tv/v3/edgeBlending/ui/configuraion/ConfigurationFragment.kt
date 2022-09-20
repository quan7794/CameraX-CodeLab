package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuraion

import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.koinViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigurationFragmentBinding

class ConfigurationFragment: BaseVmDbFragment<ConfigurationFragmentBinding, ConfigurationViewModel>() {
    override fun getLayoutId(): Int = R.layout.configuration_fragment

    override val viewModel: ConfigurationViewModel by koinViewModel()
    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpData() {
        super.setUpData()
    }
}