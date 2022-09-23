package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.introduction

import android.view.View
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.koinViewModel
import com.samsung.android.architecture.ext.observe
import com.samsung.android.architecture.ext.replaceFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.IntroductionFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.mainconfig.MainConfigFragment

class IntroductionFragment: BaseVmDbFragment<IntroductionFragmentBinding, IntroductionViewModel>() {
    override fun getLayoutId(): Int = R.layout.introduction_fragment

    override val viewModel: IntroductionViewModel by koinViewModel()
    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(clickEvent) {
                when (it) {
                    is IntroductionEvent.OnStartClick -> {
                        replaceFragment(MainConfigFragment(),R.id.container)
                    }
                }
            }
        }

        observe(viewModel.showHeader){
            binding.iclEbTop.root.visibility = if (it) View.VISIBLE else View.GONE
        }

    }

}