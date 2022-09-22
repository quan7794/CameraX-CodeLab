package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.koinViewModel
import com.samsung.android.architecture.ext.observe
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.EBDeviceAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.GalleryAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigSearchDeviceFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.util.ImageUtil
import org.koin.core.parameter.parametersOf


class SearchDeviceFragment: BaseVmDbFragment<ConfigSearchDeviceFragmentBinding, SearchDeviceViewModel>() {
    override fun getLayoutId(): Int = R.layout.config_search_device_fragment

    override val viewModel: SearchDeviceViewModel by koinViewModel{ parametersOf(requireActivity())}
    private var ebDeviceAdapter: EBDeviceAdapter ? =null

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)


        binding.rclDevice.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)

        ebDeviceAdapter = EBDeviceAdapter{ pos, device ->
         viewModel.setPhotoSelected(pos, device, ebDeviceAdapter!!.getCurrentList())
        }

        binding.rclDevice.adapter = ebDeviceAdapter
        viewModel.setUpSearchStateFlow(binding.searchView)
    }

    override fun setUpData() {
        super.setUpData()
        viewModel.getNearbyDevices()
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(state) {
                when (it) {
                    is SearchDeviceState.SearchResult -> {
                        ebDeviceAdapter?.swapItems(it.devices)
                    }
                }
            }
        }

        observe(viewModel.showHeader){
            binding.iclEbTop.topContentName.text = getString(R.string.COM_SID_EB_CONFIGURATION_BLENDING_DEVICE)
            binding.iclEbTop.root.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

}