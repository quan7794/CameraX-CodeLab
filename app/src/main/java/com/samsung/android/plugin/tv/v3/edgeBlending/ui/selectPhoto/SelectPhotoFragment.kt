package com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.samsung.android.architecture.base.BaseVmDbFragment

import com.samsung.android.architecture.ext.*
import com.samsung.android.architecture.library.pickimage.data.GalleryRepositoryImpl
import com.samsung.android.plugin.tv.v3.edgeBlending.R
import com.samsung.android.plugin.tv.v3.edgeBlending.adapter.GalleryAdapter
import com.samsung.android.plugin.tv.v3.edgeBlending.databinding.SelectPhotoFragmentBinding
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.editPhoto.EditPhotoFragment
import com.samsung.android.plugin.tv.v3.edgeBlending.ui.selectPhoto.model.PhotoModel
import com.samsung.android.plugin.tv.v3.edgeBlending.util.*
import com.samsung.android.plugin.tv.v3.edgeBlending.util.PermissionUtil.Companion.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE

class SelectPhotoFragment : BaseVmDbFragment<SelectPhotoFragmentBinding, SelectPhotoViewModel>() {

    override fun getLayoutId() = R.layout.select_photo_fragment
    override val viewModel: SelectPhotoViewModel by lazy {
        getNormalViewModel(
            SelectPhotoViewModel(
            GalleryRepositoryImpl(
                requireActivity().applicationContext,
            )))

    }
    private val permissionUtil: PermissionUtil by lazy { PermissionUtil() }

    private var galleryAdapter: GalleryAdapter? =null
    private var allPhotos: ArrayList<PhotoModel.Data> = ArrayList()

    private val spinnerListener: AdapterView.OnItemSelectedListener =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, l: Long) {
                viewModel.getAlbum(pos)
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }


    override fun setBindingVariables() {
        super.setBindingVariables()
        binding.viewModel = viewModel
    }

    override fun setUpViews(savedInstanceState: Bundle?) {
        super.setUpViews(savedInstanceState)
        binding.gridPhoto.layoutManager = GridLayoutManager(requireActivity(), 4)
        binding.spinnerPhotoGroup?.onItemSelectedListener = spinnerListener
        binding.spinnerPhotoGroup.dropDownHorizontalOffset =
            ImageUtil.getPixels(
                requireActivity(),
                DROPDOWN_HORIZONTAL_OFFSET
            )

           galleryAdapter = GalleryAdapter(MAX_SELECTED_PHOTO, false){ pos, photo ->
               viewModel.selectedPhoto()
           }

        binding.gridPhoto.itemAnimator = null
        binding.gridPhoto.adapter = galleryAdapter

    }


    override fun setUpData() {
        super.setUpData()
        permissionUtil.checkPermissions(this) {
            viewModel.getAlbums(requireContext())
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
          PERMISSION_REQUEST_READ_EXTERNAL_STORAGE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logD(
                    "onRequestPermissionsResult()",
                    "PERMISSION_GRANTED"
                )
                viewModel.getAlbums(requireContext())
            } else {
                if (activity == null) return
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        requireActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                   logD(
                        "onRequestPermissionsResult()",
                        "External Storage Permission Denied"
                    )
                   // finishFragment(null)
                } else {
                logD(
                        "onRequestPermissionsResult()",
                        "External Storage Permission Denied Permanently"
                    )
                    permissionUtil.showPermissionRequiredDialog(requireContext()){
                        //finishFragment(null)
                    }
                }
            }
            else -> {
            }
        }
    }

    override fun setUpObservers() {
        super.setUpObservers()
        with(viewModel){
            observe(state){ it ->
                when(it) {
                    is SelectPhotoState.GetAlbumNames -> {
                        logD("getPhoto", "Entry")
                        setUpSpinner(it.albumNames)
                    }

                    is SelectPhotoState.GetAlbum -> {
                        logD("getAlbum", "Entry")
                        binding.txtAlbumSelected.text = it.albumName
                        galleryAdapter?.setData(viewModel.allPhotos, it.photoOfAlbum)

                    }

                    is SelectPhotoState.SelectedPhoto -> {
                        if (it.selectedImageList.isNotEmpty()) {
                            binding.layoutBottomArea.Visible()
                        } else {
                            binding.layoutBottomArea.Gone()
                        }
                        val numSelected = it.selectedImageList.size
                        binding.selectedPhotoNumber.text = "$numSelected/${MAX_SELECTED_PHOTO}"
                        if (numSelected == MAX_SELECTED_PHOTO) {
                            Toast.makeText(context, requireContext().getString(R.string.MAPP_SID_AMBIENT_AJDUSTPHOTO), Toast.LENGTH_SHORT).show()
                        }

                    }

                }
            }
        }

        with(viewModel){
            observe(clickEvent){
                when(it) {
                    is SelectPhotoState.GotoCropPhotoScreen -> {
                        replaceFragment(EditPhotoFragment.newInstance(it.uris),R.id.container,EditPhotoFragment.TAG)
                    }
                    is SelectPhotoState.AlbumClick -> {
                        binding.spinnerPhotoGroup.performClick()
                    }
                }
            }
        }
    }


    private fun setUpSpinner(albumNames: MutableList<String>) {
        val spinnerAdapter: ArrayAdapter<*> =
            ArrayAdapter(requireContext(), R.layout.item_spinner, albumNames)
        binding.spinnerPhotoGroup.adapter = spinnerAdapter
        spinnerAdapter.notifyDataSetChanged()
        if (viewModel.getPrevAlbumAt() != 0) {
            binding.spinnerPhotoGroup.setSelection(viewModel.getPrevAlbumAt())
        }
    }

    override fun onDestroyView() {
        with(binding){
            gridPhoto.adapter = null
            spinnerPhotoGroup.adapter = null
        }

        super.onDestroyView()
    }

    companion object {
        val Tag: String by getTagName()
        private const val DROPDOWN_HORIZONTAL_OFFSET = 16
        val MAX_SELECTED_PHOTO = 50
        val KEY_URI = "URI"
    }

}