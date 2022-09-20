package com.samsung.android.architecture.ext

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer


fun <T> Fragment.observe(liveData: LiveData<T>?, observer: (T) -> Unit) =
    liveData?.observe(viewLifecycleOwner, Observer(observer))

fun <T> Fragment.observeUtilTrue(liveData: LiveData<T>?, observer: (T) -> Unit) =
    liveData?.observe(viewLifecycleOwner, object : Observer<T> {
        override fun onChanged(value: T) {
            if (value is Boolean) {
                if (value) {
                    liveData.removeObserver(this)
                    observer(value)
                }
            }
        }
    })


inline fun <FRAGMENT : Fragment> FRAGMENT.putArgs(argsBuilder: Bundle.() -> Unit): FRAGMENT = this.apply { arguments = Bundle().apply(argsBuilder) }

