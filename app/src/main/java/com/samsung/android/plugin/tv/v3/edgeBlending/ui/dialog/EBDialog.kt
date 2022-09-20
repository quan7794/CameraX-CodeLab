package com.samsung.android.plugin.tv.v3.edgeBlending.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Display
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView

import com.samsung.android.architecture.base.ILogger
import com.samsung.android.architecture.ext.injectObject
import com.samsung.android.plugin.tv.v3.edgeBlending.R


class EBDialog private constructor(context: Context?, builder: EBDialogBuilder) :
    Dialog(
        context!!
    ) {
    private var title: String? = null
    private var description: String? = null
    private var okButtonText: String? = null
    private var cancelButtonText: String? = null
    private var errorDescription: String? = null
    private var screenId = ""
    private var userOkListener: View.OnClickListener? = null
    private var userCancelListener: View.OnClickListener? = null
    private var cancelable: Boolean? = null
    val mLogger: ILogger by injectObject()

    enum class DialogType {
        ONE_BUTTON, TWO_BUTTON, PROGRESS_BAR, SERVER_ERROR
    }

    private var dialogType: DialogType? = null
    private fun assignValues(builder: EBDialogBuilder) {
        dialogType = builder.dialogType
        screenId = builder.screenId
        title = builder.title
        description = builder.description
        errorDescription = builder.errorDescription
        cancelButtonText = builder.cancelButtonText
        okButtonText = builder.okButtonText
        userCancelListener = builder.userCancelListener
        userOkListener = builder.userOkListener
        cancelable = builder.cancelable
    }

    fun setButtonClickListener(okButtonClickListener: View.OnClickListener?) {
        userOkListener = okButtonClickListener
    }

    private val okButtonClickListener = View.OnClickListener { view ->
        sendOkSALog()
        if (userOkListener != null) {
            mLogger.d(TAG, "onClick()", "userOkListener != null")
            userOkListener!!.onClick(view)
        }
        dismiss()
    }
    private val cancelButtonClickListener = View.OnClickListener { view ->
        sendCancelSALog()
        if (userCancelListener != null) {
            userCancelListener!!.onClick(view)
        }
        dismiss()
    }

    private fun sendOkSALog() {
        if (!screenId.isEmpty()) {

            mLogger.d(TAG, "sendOkSALog()", "entering..")
        }
    }

    private fun sendCancelSALog() {
        if (!screenId.isEmpty()) {
            mLogger.d(TAG, "sendCancelSALog()", "entering..")
        }
    }

    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.eb_dialog)
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (cancelable != null) {
            setCancelable(cancelable!!)
        } else {
            setCancelable(false)
        }
        //        saLogSender = LifeStyleInjector.getLifeStyleSALogSender();
        initViews()
    }

    override fun show() {
        super.show()
        val params = window!!.attributes
        val display: Display = window!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val scale = dialogWidthScale
        mLogger.d(TAG, "show()", "dialog width scale: $scale")
        params.width = (size.x * scale).toInt()
        window!!.setGravity(Gravity.BOTTOM)
        window!!.attributes = params
    }

    private val dialogWidthScale: Float
        private get() {
            val typedValue = TypedValue()
            context.resources.getValue(R.dimen.ambient_dialog_width_scale, typedValue, true)
            return typedValue.getFloat()
        }

    private fun initViews() {
        val titleView = findViewById<TextView>(R.id.dialog_title)
        if (title!!.isEmpty()) {
            titleView.visibility = View.GONE
        } else {
            titleView.text = title
        }
        val descriptionView = findViewById<TextView>(R.id.dialog_description)
        descriptionView.text = description
        val okButton = findViewById<TextView>(R.id.dialog_button_ok)
        if (!okButtonText!!.isEmpty()) {
            okButton.text = okButtonText
        }
        val cancelButton = findViewById<TextView>(R.id.dialog_button_cancel)
        if (!cancelButtonText!!.isEmpty()) {
            cancelButton.text = cancelButtonText
        }
        val progressbarLayout: LinearLayout = findViewById(R.id.layout_progressbar)
        when (dialogType) {
            DialogType.ONE_BUTTON -> {
            }
            DialogType.TWO_BUTTON -> {
                cancelButton.visibility = View.VISIBLE
                if (cancelable == null) {
                    setCancelable(true)
                }
                findViewById<View>(R.id.divider_button).visibility = View.VISIBLE
            }
            DialogType.PROGRESS_BAR -> progressbarLayout.setVisibility(View.VISIBLE)
            DialogType.SERVER_ERROR -> setErrorDescription()
            else -> {
            }
        }
        okButton.setOnClickListener(okButtonClickListener)
        okButton.contentDescription =
            okButton.text.toString() + ", " + context.getString(R.string.COM_SID_IOTCONTROL_BUTTON)
        cancelButton.setOnClickListener(cancelButtonClickListener)
        cancelButton.contentDescription =
            cancelButton.text.toString() + ", " + context.getString(R.string.COM_SID_IOTCONTROL_BUTTON)
    }

    private fun setErrorDescription() {
        val errorDescriptionView = findViewById<TextView>(R.id.txt_error_description)
        val error = "($errorDescription)"
        errorDescriptionView.text = error
        errorDescriptionView.visibility = View.VISIBLE
    }

    class EBDialogBuilder(private val context: Context?, val dialogType: DialogType) {
        var title = ""
        var description = ""
        var okButtonText = ""
        var cancelButtonText = ""
        var errorDescription = ""
        var screenId = ""

        //        private SAInfo okSAInfo, cancelSAInfo;
        var userOkListener: View.OnClickListener? = null
        var userCancelListener: View.OnClickListener? = null
        var cancelable: Boolean? = null
        fun screenId(screenId: String): EBDialogBuilder {
            this.screenId = screenId
            return this
        }

        fun title(titleId: Int): EBDialogBuilder {
            title = context!!.getString(titleId)
            return this
        }

        fun title(title: String): EBDialogBuilder {
            this.title = title
            return this
        }

        fun errorDescription(error: String): EBDialogBuilder {
            errorDescription = error
            return this
        }

        fun description(descriptionId: Int): EBDialogBuilder {
            description = context!!.getString(descriptionId)
            return this
        }

        fun description(description: String): EBDialogBuilder {
            this.description = description
            return this
        }

        fun description(topDescriptionId: Int, bottomDescription: String): EBDialogBuilder {
            description = """
                   ${context!!.getString(topDescriptionId)}
                   $bottomDescription
                   """.trimIndent()
            return this
        }

        fun description(topDescriptionId: Int, bottomDescriptionId: Int): EBDialogBuilder {
            description = """
                   ${context!!.getString(topDescriptionId)}
                   ${context.getString(bottomDescriptionId)}
                   """.trimIndent()
            return this
        }


        fun buttonText(cancelButtonId: Int, okButtonId: Int): EBDialogBuilder {
            cancelButtonText = context!!.getString(cancelButtonId)
            okButtonText = context.getString(okButtonId)
            return this
        }


        fun buttonText(buttonId: Int): EBDialogBuilder {
            okButtonText = context!!.getString(buttonId)
            return this
        }

        fun buttonClickListener(okButtonClickListener: View.OnClickListener?): EBDialogBuilder {
            userOkListener = okButtonClickListener
            return this
        }

        fun buttonClickListener(
            cancelButtonClickListener: View.OnClickListener?,
            okButtonClickListener: View.OnClickListener?
        ): EBDialogBuilder {
            if (cancelButtonClickListener != null) {
                userCancelListener = cancelButtonClickListener
            }
            if (okButtonClickListener != null) {
                userOkListener = okButtonClickListener
            }
            return this
        }

        fun cancelable(cancelable: Boolean?): EBDialogBuilder {
            this.cancelable = cancelable
            return this
        }

        fun build(): EBDialog {
            return EBDialog(context, this)
        }

        fun show(): EBDialog {
            val dialog = build()
            dialog.show()
            return dialog
        }

        init {

            when (dialogType) {
                DialogType.ONE_BUTTON -> {
                }
                DialogType.TWO_BUTTON -> {
                }
                DialogType.PROGRESS_BAR -> {
                    title = context!!.getString(R.string.COM_COMMONCTRL_SEND)
                   // description = getTextSendingContentToDevice(context)
                    okButtonText = context.getString(R.string.COM_SID_CANCEL_ABBR_7)
                }
                DialogType.SERVER_ERROR -> {
                    title = context!!.getString(R.string.COM_SID_ERROR_KR_ERROR)
                   // description = getTextAnErrorOccurredWithMobileAnDevice(context)
                }

            }
        }
    }

    companion object {
        private const val TAG = "EBDialog"
        private const val DIALOG_SCALE = 1.0
    }

    init {
        assignValues(builder)
    }
}