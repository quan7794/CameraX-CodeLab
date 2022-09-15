package com.android.samsung.cameraxcodelab.camera

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.samsung.cameraxcodelab.databinding.FragmentCameraBinding
import com.jintin.bindingextension.BindingFragment
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : BindingFragment<FragmentCameraBinding>() {
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    private lateinit var viewModel: CameraViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CameraViewModel::class.java]
        //request Camera permission
        if (allPermissionGranted()) startCamera()
        else ActivityCompat.requestPermissions(requireActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)

        //Set up listeners for take photo & video capture buttons.
        binding.apply {
            imageCaptureButton.setOnClickListener { takePhoto() }
        }
        //Init Async service.
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val imageFileName = "CameraX-${SimpleDateFormat(FILENAME_FORMAT, Locale.US).format(System.currentTimeMillis())}"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, imageFileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P)
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(requireActivity().contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(requireContext()), object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(requireActivity().baseContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(TAG, msg)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
            }
        })

    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        with(cameraProviderFuture) {
            addListener( {
                val cameraProvider =  cameraProviderFuture.get()

                //preview
                val preview = Preview.Builder().build().also { it.setSurfaceProvider(binding.viewFinder.surfaceProvider) }
                imageCapture = ImageCapture.Builder().build()
                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    //unbind last and rebind
                    cameraProvider.unbindAll()
                    //bind
                    cameraProvider.bindToLifecycle(this@CameraFragment, cameraSelector, preview, imageCapture)
                } catch (ex: Exception) {
                    Log.e(TAG, "Use case binding failed", ex)
                }

            }, ContextCompat.getMainExecutor(requireContext()))
        }


    }

    private fun allPermissionGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS)
            if (allPermissionGranted()) startCamera()
            else Toast.makeText(context, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show().also {
                requireActivity().finish()
            }
    }

    private val orientationEventListener by lazy {
        object : OrientationEventListener(requireContext()) {
            override fun onOrientationChanged(orientation: Int) {
                if (orientation == ORIENTATION_UNKNOWN) return
                updateUiComponent(orientation)
            }
        }
    }

    private fun updateUiComponent(orientation: Int) {
        var surfaceRotation = Surface.ROTATION_0
        var uiRotation = 0
        when (orientation) {
            in 45 until 135 -> {
                surfaceRotation = Surface.ROTATION_270
                uiRotation = 270
            }
            in 135 until 225 -> {
                Surface.ROTATION_180
                uiRotation = 180
            }
            in 225 until 315 -> {
                Surface.ROTATION_90
                uiRotation = 90
            }
            else -> {}
        }
        if (binding.imageCaptureButton.rotation != uiRotation.toFloat()) {
            Log.d(TAG, "updateUiComponent: orientation = $orientation, uiRotation = $uiRotation.")
            imageCapture?.targetRotation = surfaceRotation
            binding.imageCaptureButton.rotation = uiRotation.toFloat()
        }
    }

    override fun onStart() {
        super.onStart()
        orientationEventListener.enable()
    }

    override fun onStop() {
        super.onStop()
        orientationEventListener.disable()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "onConfigurationChanged")
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        fun newInstance() = CameraFragment()
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = mutableListOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }.toTypedArray()
    }

}