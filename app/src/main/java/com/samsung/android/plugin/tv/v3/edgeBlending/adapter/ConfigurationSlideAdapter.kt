package com.samsung.android.plugin.tv.v3.edgeBlending.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.captureimage.CaptureImageFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.confirm.ConfigConfirmFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.connectdevice.ConnectDeviceFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.guide.GuideFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.introduction.IntroductionFragment

class ConfigurationSlideAdapter (fa: FragmentActivity) :
    FragmentStateAdapter(fa) {


    override fun getItemCount(): Int {
       return 5
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> GuideFragment.newInstance(R.string.COM_SID_EB_CONFIGURATION_MAIN_DESCRIPTION)
            1 -> ConnectDeviceFragment()
            2 ->  GuideFragment.newInstance(R.string.COM_SID_EB_CONFIGURATION_BLENDING_PLACE_AND_ADJUST)
            3 -> CaptureImageFragment()
            else -> ConfigConfirmFragment()
        }

    }
}