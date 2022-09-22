package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice

import android.os.Bundle
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.koinViewModel
import com.samsung.android.architecture.ext.observe
import com.samsung.android.architecture.ext.replaceFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigConnectDeviceFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.SearchDeviceFragment

class ConnectDeviceFragment: BaseVmDbFragment<ConfigConnectDeviceFragmentBinding, ConnectDeviceViewModel>() {
    override fun getLayoutId(): Int = R.layout.config_connect_device_fragment

    override val viewModel: ConnectDeviceViewModel by koinViewModel()

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.txtAppName.text = String.format(getString(R.string.COM_SID_EB_CONFIGURATION_CONNECT_TWO_DEVICE),"TVPlugin")
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(clickEvent) {
                when (it) {
                    is ConnectDeviceEvent.onSelectDevice -> {
                       replaceFragment(SearchDeviceFragment(),R.id.container, SearchDeviceFragment::class.java.simpleName)
                    }
                }
            }
        }

        with(viewModel){
            observe(state) {
                when (it) {
                    is ConnectDeviceState.SetDevice -> {
                        binding.device = it.device
                    }
                }
            }
        }
    }

}