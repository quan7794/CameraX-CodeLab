package com.samsung.android.architecture.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

import com.samsung.android.architecture.ext.*


abstract class BaseVmFragment<VM : ViewModel> : Fragment() {

    abstract val viewModel: VM
    protected val TAG: String by getTagName()

    val logger: ILogger by injectObject()

    private var mRootView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logD("onCreate", "Entry")
        getFragmentArguments()
        initOnCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        logD("onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        logD("onViewCreated", "ENTRY")
        setBindingVariables()
        setUpViews(savedInstanceState)
        setUpObservers()
        setUpData()
    }

    open fun getFragmentArguments() {}

    open fun initOnCreate(savedInstanceState: Bundle?) {}

    open fun setBindingVariables() {}

    open fun setUpViews(savedInstanceState: Bundle?) {}

    open fun setUpData() {}

    open fun setUpObservers() {}

}