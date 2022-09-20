package com.samsung.android.architecture.base



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.samsung.android.architecture.R
import com.samsung.android.architecture.event.SingleLiveEvent
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.logD
import com.samsung.android.architecture.ext.observe

abstract class BaseVmDbFragment<DB : ViewBinding, VM : ViewModel> : BaseVmFragment<VM>() {

    private var _binding: DB? = null

    val binding get() = _binding!!

    private var mRootView: View? = null
    private var hasInitializedRootView = false


    @LayoutRes
    abstract fun getLayoutId(): Int


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logD("onCreateView")
        initViewBinding(inflater, container)
        return mRootView
    }

    private fun initViewBinding(inflater: LayoutInflater, container: ViewGroup?) {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        logD("initViewBinding")
        mRootView = binding?.root
    }

    override fun setUpObservers() {

        val progressBar: SingleLiveEvent<Boolean> = if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).progressLiveEvent
        } else {
            (viewModel as BaseAndroidViewModel).progressLiveEvent
        }
        observe(progressBar) { show ->
            if (show) showLoading()
            else hideLoading()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
       if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).onDestroyView()
        } else {
            (viewModel as BaseAndroidViewModel).onDestroyView()
        }

    }

    protected fun hideLoading() {
        _binding?.root?.findViewById<ProgressBar>(R.id.progress_bar)?.let {
            it.visibility = View.GONE
        }
    }

    protected fun showLoading() {
        _binding?.root?.findViewById<ProgressBar>(R.id.progress_bar)?.let {
            it.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        logD("onAttach", "context $context")
    }

    override fun onDetach() {
        super.onDetach()
        logD("onDetach", "Fragment is detached")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD("onDestroy", "Fragment is destroy")
    }

}