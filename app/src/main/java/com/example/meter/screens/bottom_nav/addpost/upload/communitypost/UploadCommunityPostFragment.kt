package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.Manifest
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.adapter.UploadCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCommunityPostFragmentBinding
import com.example.meter.extensions.loadImgUri
import com.example.meter.extensions.toFile
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody


@AndroidEntryPoint
class UploadCommunityPostFragment() :
    BaseFragment<UploadCommunityPostFragmentBinding, UploadCommunityPostViewModel>(
        UploadCommunityPostFragmentBinding::inflate,
        UploadCommunityPostViewModel::class.java
    ) {


    private var uri: Uri? = null

    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val photoFileList: MutableList<ByteArray> = mutableListOf()

    private lateinit var adapter: UploadCommunityPostPhotoRecyclerAdapter


    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        adapter = UploadCommunityPostPhotoRecyclerAdapter()
        binding.recyclerPhotos.adapter = adapter
        binding.recyclerPhotos.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)

        binding.addImage.setOnClickListener {

            if (hasCamera() && hasRead()) {
                openCamera()
            } else {
                requestPermission(requestPermissionResult)
            }
        }

        binding.save.setOnClickListener {
            viewModel.uploadPost("5", "maskarpone new", "PLIS NOTICE ME SNEPAI",photoFileList)
        }
    }


    val requestPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA] == true && it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                openCamera()
            }
        }

    private fun openCamera() {

        val fileName = "profile.jpg"

        val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val imageDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
        }


        requireActivity().contentResolver.insert(imageUri, imageDetails).let {
            uri = it
            takePic.launch(uri)
        }

    }

    private val takePic = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        binding.userImage.loadImgUri(uri)
        uri?.let { it1 -> photoUriList.add(it1) };
        adapter.submitData(photoUriList)
        val file = uri?.toFile(requireContext())

        val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())

        val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
        photoFileList.add(file.readBytes())
    }


//    val startForResult =
//        registerForActivityResult(StartActivityForResult()) { result: ActivityResult ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                val intent = result.data
//                if (intent != null) {
//                    imageUri = intent.data!!
//                    binding.userImage.loadImgUri(imageUri)
//
//                    i("imageUri", "$imageUri")
//                }
//            }
//        }
}