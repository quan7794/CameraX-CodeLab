package com.android.samsung.cameraxcodelab

import android.os.Bundle
import android.os.PersistableBundle
import com.android.samsung.cameraxcodelab.crop.CropFragment
import com.android.samsung.cameraxcodelab.databinding.ActivityMainBinding
import com.jintin.bindingextension.BindingActivity

class MainActivity : BindingActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, CropFragment.newInstance(), CropFragment::class.simpleName)
            .addToBackStack(CropFragment::class.simpleName)
            .commit()
    }
}