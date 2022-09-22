package com.samsung.android.plugin.tv.v3.edgeBlending.ui.configuration.mainconfig

import com.samsung.android.architecture.base.UIState

sealed class MainConfigEventState: UIState {
    class UpdateScreen(val pos:Int, val configModel: ConfigModel): MainConfigEventState()

}
