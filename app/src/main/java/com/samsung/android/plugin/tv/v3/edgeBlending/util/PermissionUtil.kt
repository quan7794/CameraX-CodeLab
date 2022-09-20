package com.samsung.android.plugin.tv.v3.edgeBlending.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.samsung.android.architecture.base.ILogger
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.injectObject
import com.samsung.android.plugin.tv.v3.edgeBlending.R

import com.samsung.android.plugin.tv.v3.edgeBlending.ui.dialog.EBDialog

class PermissionUtil {
     val logger:ILogger by injectObject()
     val Tag: String by getTagName()

    companion object {
        val PERMISSION_REQUEST_READ_EXTERNAL_STORAGE = 10
    }

    fun showPermissionRequiredDialog(context: Context, action: () -> Unit) {
        EBDialog.EBDialogBuilder(
            context,
            EBDialog.DialogType.ONE_BUTTON
        )
            .title(R.string.MAPP_SID_CORE_CAMAR_PERMISSION_REQUIRED)
            .description(R.string.MAPP_SID_CORE_CAMAR_TO_USE_ALLOW_PERMIS_IN_ST)
            .buttonClickListener(View.OnClickListener { v: View? ->
                action.invoke()
            })
            .show()
    }

   fun checkPermissions(fragment: Fragment, action: () -> Unit) {
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            logger.d(Tag,
                "checkPermissions()",
                "Permission granted for READ_EXTERNAL_STORAGE")
            action.invoke()

        } else {
            logger.d(Tag,
                "checkPermissions()",
                "Request permission for READ_EXTERNAL_STORAGE"
            )
            fragment.requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }
}