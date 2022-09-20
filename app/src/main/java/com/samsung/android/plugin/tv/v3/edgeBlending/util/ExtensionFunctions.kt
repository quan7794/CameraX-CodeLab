package com.samsung.android.plugin.tv.v3.edgeBlending.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class ExtensionFunctions {
}

fun FragmentManager.getFragmentByTag(tag: String): Fragment? {
    return this.findFragmentByTag(tag)
}

fun <T> LiveData<T>.observeUntilTrue(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            if (value is Boolean) {
                if (value) {
                    removeObserver(this)
                    observer(value)
                }
            }
        }
    })
}
