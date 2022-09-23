package com.samsung.android.plugin.tv.v3.edgeBlending.domain.model

import android.text.Html
import android.text.Spanned

data class EbConfigure(val connectedTo: String? = null, val ratio: String? = null, val configTime: String? = null) {
    fun toFormattedText(): Spanned {
        return if (isNotConfig()) Html.fromHtml("<b>Not Configured</b>", Html.FROM_HTML_MODE_LEGACY)
        else Html.fromHtml("Connected : <b>$connectedTo</b> <br />" +
                "Blended ratio : <b>$ratio</b> <br />" +
                "Last configured : <b>$configTime</b>", Html.FROM_HTML_MODE_LEGACY
        )
    }
    
    fun isNotConfig() = (connectedTo == null && ratio == null && configTime == null)
}
