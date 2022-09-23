package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.*
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.EBDeviceAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigSearchDeviceFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.domain.model.EbDevice
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants.REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants.REQUEST_DEVICE_INFO
import org.koin.core.parameter.parametersOf


class SearchDeviceFragment: BaseVmDbFragment<ConfigSearchDeviceFragmentBinding, SearchDeviceViewModel>() {
    override fun getLayoutId(): Int = R.layout.config_search_device_fragment

    override val viewModel: SearchDeviceViewModel by koinViewModel{ parametersOf(requireActivity())}
    private var ebDeviceAdapter: EBDeviceAdapter ? =null
    private var deviceId: String? = null

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        deviceId = arguments?.getString(REQUEST_DEVICE_INFO)
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)

        binding.rclDevice.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        ebDeviceAdapter = EBDeviceAdapter{ pos, device ->
         viewModel.setPhotoSelected(pos, device, ebDeviceAdapter!!.getCurrentList())
        }

        binding.rclDevice.adapter = ebDeviceAdapter
        binding.searchView.setOnClickListener {
            binding.searchView.isIconified = false
        }
        viewModel.setUpSearchStateFlow(binding.searchView)
    }

    override fun setUpData() {
        super.setUpData()
        viewModel.getNearbyDevices(deviceId)
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(state) {
                when (it) {
                    is SearchDeviceState.SearchResult -> {
                        ebDeviceAdapter?.swapItems(it.devices)
                    }

                    is SearchDeviceState.SelectedDevice -> {
                        ebDeviceAdapter?.swapItems(it.devices)
                    }

                }
            }
        }

        observe(viewModel.clickEvent) {
            when (it) {
                is SearchDeviceEvent.ON_PREVIOUS_CLICK -> {
                    sendDeviceInfo(it.ebDevice)
                    navigateUp()
                }

                is SearchDeviceEvent.ON_NEXT_CLICK -> {
                    sendActionNext(it.ebDevice, true)
                    navigateUp()
                }
            }
        }

        observe(viewModel.showHeader){
            binding.iclEbTop.topContentName.text = getString(R.string.COM_SID_EB_CONFIGURATION_BLENDING_DEVICE)
            binding.iclEbTop.root.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

   private fun sendDeviceInfo(ebDevice: EbDevice?) {
        setFragmentResult(REQUEST_DEVICE_INFO, bundleOf(REQUEST_DEVICE_INFO  to ebDevice))
    }

    private fun sendActionNext(ebDevice: EbDevice?, isNextStep: Boolean = false) {
        setFragmentResult(
            REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK, bundleOf(
                REQUEST_DEVICE_INFO to ebDevice,
                REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK to isNextStep
            )
        )
    }

    companion object {

        fun newInstance(deviceId: String?) = SearchDeviceFragment().putArgs {
            putString(REQUEST_DEVICE_INFO , deviceId)
        }
    }

}