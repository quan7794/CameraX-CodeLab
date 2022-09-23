package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.mainconfig

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import com.samsung.android.architecture.base.BaseViewModel
import com.samsung.android.plugin.tv.v3.edgeBlending.R

class MainConfigViewModel: BaseViewModel() {
    private var indicatorCount: Int = 0
    private var indicatorView: Array<ImageView?>? = null
    private var _stepIndex = 0


    val headerResId: MutableList<Int> = arrayListOf(R.string.COM_SID_EB_CONFIGURATION_MAIN_HEADER,
    R.string.COM_SID_EB_CONFIGURATION_STEP_TWO_HEADER,R.string.COM_SID_EB_CONFIGURATION_MAIN_HEADER,
    R.string.COM_SID_EB_CONFIGURATION_MAKE_BLENDING_AREA, R.string.COM_SID_EB_CONFIGURATION_CONFIRM_BLENDING_AREA)

    fun setData(context: Context?) {
        _clickEvent.value = MainConfigEventState.UpdateScreen(_stepIndex, getConfigModel(_stepIndex, context))
        updateIndicator(_stepIndex, context)
    }

    private fun getConfigModel(position: Int, context: Context?): ConfigModel {
        val title = context?.getString(headerResId[position])
        return when (position) {
            0 -> {
                ConfigModel(
                    title, context?.getString(R.string.COM_SID_EB_CONFIGURATION_CANCEL),
                    context?.getString(R.string.COM_SID_EB_CONFIGURATION_NEXT)
                )
            }

            1 ,2 -> {
                ConfigModel(
                    title, context?.getString(R.string.COM_SID_EB_CONFIGURATION_PREVIOUS),
                    context?.getString(R.string.COM_SID_EB_CONFIGURATION_NEXT)
                )
            }

            3 -> {
                ConfigModel(
                    title, context?.getString(R.string.COM_SID_EB_CONFIGURATION_CANCEL),
                    context?.getString(R.string.COM_SID_EB_CONFIGURATION_BLEND),3
                )
            }

            else -> {
                ConfigModel(
                    title, context?.getString(R.string.COM_SID_EB_CONFIGURATION_RETAKE),
                    context?.getString(R.string.COM_SID_EB_CONFIGURATION_CONFIRM), 4
                )
            }
        }

    }


    fun removeIndicator() {
        if (indicatorView != null && indicatorView!![0] != null) {
            (indicatorView!![0]?.parent as LinearLayout).removeAllViews()

        }
    }

    fun setUiPageViewController(view: LinearLayout, context: Context?) {
        indicatorCount = 5
        if (indicatorView == null) {
            indicatorView = arrayOfNulls<ImageView>(indicatorCount)

            if (indicatorCount <= 1) {
                return
            }

            for (i in 0 until indicatorCount) {
                indicatorView!![i] = ImageView(context)
                indicatorView!![i]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.nonselecteditem_dot))

                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                params.gravity = Gravity.BOTTOM
                params.setMargins(30, 0, 30, 10)

                view.addView(indicatorView!![i], params)
            }
            if (indicatorView != null && indicatorView!!.size > 0) {
                indicatorView!![0]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.selecteditem_dot))
            }
        } else {

            indicatorView!!.forEach {
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                )

                params.gravity = Gravity.BOTTOM
                params.setMargins(30, 0, 30, 10)
                it?.let {
                    view.addView(it, params)
                }
            }
        }
    }

   private fun updateIndicator(position: Int, context: Context?) {
        for (i in 0 until indicatorCount) {
            indicatorView!![i]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.nonselecteditem_dot))
        }

        indicatorView!![position]?.setImageDrawable(context?.resources?.getDrawable(R.drawable.selecteditem_dot))
       _stepIndex = position
    }

    fun onNextClick(context: Context) {
        if (_stepIndex < 4) {
            _stepIndex++
            _clickEvent.value = MainConfigEventState.UpdateScreen(_stepIndex, getConfigModel(_stepIndex, context))
            updateIndicator(_stepIndex, context)
        } else {
            _clickEvent.value = MainConfigEventState.ConfirmButtonClick(_stepIndex, getConfigModel(_stepIndex, context))
        }
    }

    fun onPreviousClick(context: Context) {
        if (_stepIndex > 0) {
            _stepIndex--
            _clickEvent.value = MainConfigEventState.UpdateScreen(_stepIndex, getConfigModel(_stepIndex, context))
            updateIndicator(_stepIndex, context)
        }
    }

    fun blendClick() {

    }

    fun confirmClick() {

    }

}