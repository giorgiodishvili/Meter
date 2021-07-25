package com.example.meter.base

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.meter.extensions.showDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding, VM : ViewModel>(
    private val inflate: Inflate<VB>,
    private val baseViewModel: Class<VM>
) : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected lateinit var dialogItem: Dialog
    protected val viewModel: VM by lazy {
        ViewModelProvider(this).get(baseViewModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        setUp(inflater, container)
        return binding.root
    }

    abstract fun setUp(inflater: LayoutInflater, container: ViewGroup?)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun popDialog(layout: Int, tv: Int?=null, text:String="", autoClose: Boolean?=false) {
        dialogItem = Dialog(requireActivity())
        dialogItem.showDialog(layout)
        if (autoClose == true) {
            CoroutineScope(Dispatchers.Main).launch {
                delay(4200)
                dialogItem.cancel()
            }
        }
        if (tv != null) {
            dialogItem.findViewById<TextView>(tv).text = text
        }
        dialogItem.show()
    }


    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
    }

    fun hasCamera() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    fun hasRead() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    fun hasWrite() = ActivityCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED


    fun requestPermission(request: ActivityResultLauncher<Array<String>>) {
        request.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )

    }

}