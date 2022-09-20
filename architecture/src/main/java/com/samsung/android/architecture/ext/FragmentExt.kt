package com.samsung.android.architecture.ext

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.architecture.base.BaseVmFragment
import com.samsung.android.architecture.base.ViewModelProviderFactory

inline fun FragmentManager.inTransactionAllowingStateLoss(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

inline fun <F> AppCompatActivity.replaceFragment(
    fragment: F,
    id: Int,
    tag: String? = null
) where F : Fragment {
    supportFragmentManager.inTransactionAllowingStateLoss {
        addToBackStack(tag)
        replace(id, fragment, tag)
    }
}

inline fun <F> BaseVmDbFragment<*,*>.replaceFragment(
    fragment: F,
    id: Int,
    tag: String? = null
) where F : Fragment {
    requireActivity().supportFragmentManager.inTransactionAllowingStateLoss {
        addToBackStack(tag)
        replace(id, fragment, tag)
    }
}

inline fun <F> AppCompatActivity.addFragment(
    fragment: F,
    id: Int,
    tag: String? = null
) where F : Fragment {
    supportFragmentManager.inTransactionAllowingStateLoss {
        addToBackStack(tag)
        add(id, fragment, tag)
    }
}

inline fun <reified T : ViewModel> BaseVmFragment<*>.getNormalViewModel(vm: BaseViewModel) =
    ViewModelProvider(this, ViewModelProviderFactory(vm))[T::class.java]







