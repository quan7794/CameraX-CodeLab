package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.mainconfig

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.viewpager2.widget.ViewPager2
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.observe
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.ConfigurationSlideAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.MainConfigFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants.REQUEST_BLENDING_BUTTON_CLICK
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants.REQUEST_CONFIRM_BUTTON_CLICK
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.ConfigConstants.REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.searchdevice.SearchDeviceFragment

class MainConfigFragment: BaseVmDbFragment<MainConfigFragmentBinding, MainConfigViewModel>() {
    override fun getLayoutId(): Int = R.layout.main_config_fragment
    override val viewModel: MainConfigViewModel  by lazy { getNormalViewModel(MainConfigViewModel()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setFragmentResultListener(REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK){ key, bundle ->
            if (bundle.getBoolean(REQUEST_CONNECT_DEVICE_BUTTON_NEXT_CLICK)) {
                viewModel.onNextClick(requireContext())
            }
        }
    }

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        with(binding.viewPager){
            adapter = ConfigurationSlideAdapter(requireActivity())
            viewModel.setUiPageViewController(binding.viewPagerCountDots, context)
            isUserInputEnabled = false
        }

    }

    override fun setUpData() {
        super.setUpData()
        viewModel.setData(requireContext())
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(clickEvent) {
                when (it) {
                    is MainConfigEventState.UpdateScreen -> {
                        binding.viewPager.currentItem = it.pos
                        binding.config = it.configModel

                        if (it.pos == 4) {
                            setFragmentResult(REQUEST_BLENDING_BUTTON_CLICK, bundleOf())
                        }
                    }

                    is MainConfigEventState.ConfirmButtonClick -> {
                        setFragmentResult(REQUEST_CONFIRM_BUTTON_CLICK, bundleOf())
                    }



                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeIndicator()
    }
}