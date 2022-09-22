package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.mainconfig

import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.observe
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.ConfigurationSlideAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.MainConfigFragmentBinding

class MainConfigFragment: BaseVmDbFragment<MainConfigFragmentBinding, MainConfigViewModel>() {
    override fun getLayoutId(): Int = R.layout.main_config_fragment
    override val viewModel: MainConfigViewModel  by lazy { getNormalViewModel(MainConfigViewModel()) }

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        with(binding.viewPager){
            adapter = ConfigurationSlideAdapter(requireActivity())
            registerOnPageChangeCallback(onPageChangeCallback)
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
                    }

                }
            }
        }
    }

   private val onPageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.removeIndicator()
    }
}