package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.guide

import android.os.Bundle
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.putArgs
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.ConfigGuideFragmentBinding

class GuideFragment: BaseVmDbFragment<ConfigGuideFragmentBinding, GuideViewModel>() {

    override fun getLayoutId(): Int = R.layout.config_guide_fragment

    override val viewModel: GuideViewModel by lazy { GuideViewModel() }

    companion object {
        fun newInstance(resId: Int) = GuideFragment().putArgs {
            putInt(KEY_DESCRIPTION, resId)
        }
        val KEY_DESCRIPTION = "DESCRIPTION"
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        arguments?.getInt(KEY_DESCRIPTION).let {
            binding.txtGuideDescr.text = getString(it!!)
        }
    }

}