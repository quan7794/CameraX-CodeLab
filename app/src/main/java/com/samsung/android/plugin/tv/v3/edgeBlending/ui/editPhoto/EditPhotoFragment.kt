package com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.samsung.android.architecture.base.BaseVmDbFragment
import com.samsung.android.architecture.ext.getNormalViewModel
import com.samsung.android.architecture.ext.getTagName
import com.samsung.android.architecture.ext.observe
import com.samsung.android.architecture.ext.putArgs
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.EbEditPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.dialog.EBDialog
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.adapter.SelectedPhotosAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.util.dpToPx
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.cropImageView.view.ImageCropView
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.EditPhotoState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.state.ViewOnTvState
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.Utils
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.disableWhenViewOnTv
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.setMargin
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.util.waitForLayout
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.SelectPhotoFragment
import kotlinx.coroutines.*
import java.io.File

class EditPhotoFragment : BaseVmDbFragment<EbEditPhotoFragmentBinding, EditPhotoViewModel>() {
    override fun getLayoutId(): Int = R.layout.eb_edit_photo_fragment
    private var viewOnTvDialog: EBDialog? = null
    override val viewModel: EditPhotoViewModel by lazy { getNormalViewModel(EditPhotoViewModel()) }
    private lateinit var slideShowAdapter: SelectedPhotosAdapter
    private var viewOnTvJob: Job? = null

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        initCropView(viewModel.uriList)
        initSlideShowArea()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun initSlideShowArea() {
        slideShowAdapter = SelectedPhotosAdapter(viewModel.uriList, onSlideShowItemClick)
        binding.selectedPhotos.adapter = slideShowAdapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private val onSlideShowItemClick: (Int, Uri) -> Unit = { clickPosition, _ ->
        slideShowAdapter.apply {
            currentPosition = clickPosition
            notifyDataSetChanged()
            updateCropView(clickPosition)
            binding.selectedPhotos.smoothScrollToPosition(clickPosition)
        }
    }

    private fun updateCropView(newPosition: Int) {
        viewModel.cropStateList[slideShowAdapter.recentPosition] = binding.cropView.getPositionInfo()
        slideShowAdapter.recentPosition = newPosition
        updateCropViewState(newPosition)
    }

    private fun updateCropViewState(position: Int) = binding.cropView.apply {
        setImageUri(viewModel.uriList[position])
        viewModel.cropStateList[position]?.let { setPositionInfo(it) } ?: run { resetDisplay() }
    }

    private fun viewOnTv() {
        if (viewOnTvJob?.isActive == true) return
        viewOnTvJob = viewLifecycleOwner.lifecycleScope.launch {
            runCatching {
                updateViewOnTvState(ViewOnTvState.Start)
                viewModel.uriList.forEachIndexed { index, uri ->
                    withContext(Dispatchers.Main) { onSlideShowItemClick(index, uri) }
                    delay(300)
                    cropImage(index)
                }
                updateViewOnTvState(ViewOnTvState.Success)
            }.getOrElse { updateViewOnTvState(ViewOnTvState.Error("Message: ${it.message}")) }
        }
    }

    private fun showViewOnTvDialog() {
        val cancelAction: () -> Unit = {
            viewOnTvJob?.apply {
                cancelChildren()
                cancel()
            }
            updateViewOnTvState(ViewOnTvState.Cancel)
        }
        viewOnTvDialog = EBDialog.EBDialogBuilder(context, EBDialog.DialogType.PROGRESS_BAR)
            .title(R.string.COM_COMMONCTRL_SEND)
            .description(R.string.MAPP_SID_LUXO_CBAUG_SENDING_CONTENT_PROJECTOR)
            .cancelable(false)
            .buttonClickListener(cancelAction)
            .show()
    }

    private fun updateViewOnTvState(status: ViewOnTvState) = viewModel.updateUiState(status)

    private suspend fun cropImage(index: Int = -1) = withContext(Dispatchers.IO) {
        Log.d("cropImage", "Status: Entry_______")
        val fileName = if (index == -1) "temp" else index.toString()
        val saveDir = File(requireContext().cacheDir, "EbCache").also { if (!it.exists()) it.mkdir() }
        Log.d("cropImage", "$fileName.jpg")
        val cropImage = binding.cropView.getCroppedImage()
        val result = Utils.saveImage(cropImage, saveDir, fileName)
        if (result && viewOnTvJob?.isActive == true)
            updateViewOnTvState(ViewOnTvState.CropPercent(PERCENT_100 * ((index + 1) / viewModel.uriList.size.toFloat())))
        Log.i("cropImage", "Crop success: $result")
    }

    override fun setUpObservers() {
        super.setUpObservers()
        observe(viewModel.clickEvent) {
            when (it) {
                is EditPhotoState.ClickCropPhoto -> lifecycleScope.launch(Dispatchers.IO) { cropImage() }
                is EditPhotoState.ClickViewOnTv -> viewOnTv()
            }
        }
        observe(viewModel.state) {
            binding.editPhotoLayout.disableWhenViewOnTv(it)
            when (it) {
                is ViewOnTvState.Start -> {
                    Log.d("ViewOnTvState", "Start")
                    showViewOnTvDialog()
                }
                is ViewOnTvState.CropPercent -> {
                    Log.d("ViewOnTvState", "CropPercent: ${it.percent}")
                    viewOnTvDialog?.setProgressBar(300, it.percent.toInt())
                }
                is ViewOnTvState.Success -> {
                    Log.d("ViewOnTvState", "Success")
                    viewOnTvDialog?.dismiss()
                    Toast.makeText(context, "Send success", Toast.LENGTH_SHORT).show()
                }
                is ViewOnTvState.Cancel -> {
                    Log.d("ViewOnTvState", "Cancel")
                    Toast.makeText(context, "Send Canceled", Toast.LENGTH_SHORT).show()
                }
                is ViewOnTvState.Error -> {
                    Log.d("ViewOnTvState", "Error: ${it.cause}")
                    Toast.makeText(context, "Send Error: ${it.cause}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initCropView(uri: List<Uri>) {
        Log.d("initCropView", "Entry")
        binding.cropView.apply {
            binding.cropView.setImageUri(uri[0])
            setAspectRatio(21, 9)
            setCropViewMargin(horizontal = HORIZONTAL_CROP_VIEW_MARGIN)
            initConstraintWithCropView(22, 24)
        }
    }

    private fun ImageCropView.initConstraintWithCropView(topMarginDp: Int, bottomMarginDp: Int) = waitForLayout {
        val bottomMargin = -(mCropRect.top - dpToPx(topMarginDp) - this.top).toInt()
        val topMargin = -(this.bottom - (mCropRect.bottom + dpToPx(bottomMarginDp))).toInt()
        binding.functionArea.setMargin(top = topMargin)
        binding.descriptionText.setMargin(bottom = bottomMargin)
    }

    override fun getFragmentArguments() {
        super.getFragmentArguments()
        val selectedPhotos = arguments?.getParcelableArrayList<Uri>(SelectPhotoFragment.KEY_URI)
        selectedPhotos?.let {
            viewModel.apply {
                uriList = selectedPhotos
                setSlideAreaVisibility(uriList.size > 1)
            }
        }
    }

    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.vm = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (viewModel.state.value == ViewOnTvState.Start) updateViewOnTvState(ViewOnTvState.Error("User turn off screen."))
    }

    override fun onDestroy() {
        super.onDestroy()
        viewOnTvJob?.apply { cancel() }
    }

    companion object {
        val TAG: String by getTagName()
        const val HORIZONTAL_CROP_VIEW_MARGIN = 20
        const val PERCENT_100 = 100
        fun newInstance(uri: ArrayList<Uri>) = EditPhotoFragment().putArgs {
            putParcelableArrayList(SelectPhotoFragment.KEY_URI, uri)
        }
    }
}