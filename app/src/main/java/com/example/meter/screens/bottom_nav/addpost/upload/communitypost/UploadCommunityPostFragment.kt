package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.adapter.UploadCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCommunityPostFragmentBinding
import com.example.meter.extensions.toFile
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class UploadCommunityPostFragment() :
    BaseFragment<UploadCommunityPostFragmentBinding, UploadCommunityPostViewModel>(
        UploadCommunityPostFragmentBinding::inflate,
        UploadCommunityPostViewModel::class.java
    ) {

    private var uri: Uri? = null


    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val photoFileList: MutableList<ByteArray> = mutableListOf()

    private lateinit var adapter: UploadCommunityPostPhotoRecyclerAdapter


    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {

        initRecycler()
        setListeners()

    }

    private fun initRecycler() {
        adapter = UploadCommunityPostPhotoRecyclerAdapter()
        binding.recyclerPhotos.adapter = adapter
        binding.recyclerPhotos.layoutManager =
            LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    }

    private fun setListeners() {
        binding.uploadImage.setOnClickListener {

            if (hasCamera() && hasRead()) {
                showDialog()
            } else {
                requestPermission(requestPermissionResult)
            }
        }

        binding.save.setOnClickListener {
            if (binding.titleET.text.isNotEmpty() && binding.descriptionET.text.isNotEmpty()) {

                firebaseAuthImpl.getUserId()?.let { it1 ->
                    viewModel.uploadPost(
                        it1,
                        binding.descriptionET.text.toString(),
                        binding.titleET.text.toString(),
                        photoFileList
                    )
                }
                binding.save.isEnabled = false
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }


    val requestPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA] == true && it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                showDialog()

            }
        }

    private fun openCamera() {


        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            photoFile
        )

        takePic.launch(uri)
    }

    private val takePic = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        uri?.let { it1 -> photoUriList.add(it1) };
        adapter.submitData(photoUriList)
        val file = uri?.toFile(requireContext())
//
//        val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
//        val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
        file?.let { it1 -> photoFileList.add(it1.readBytes()) }
    }


    // Pick Images Contract - Normally this is for all kind of files.
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { uri ->
                photoUriList.add(uri);
                adapter.submitData(photoUriList)
                val file = uri.toFile(requireContext())
//
//        val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
//        val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
                file?.let { photoFileList.add(it.readBytes()) }
            }
        }


    fun showDialog() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Option")
        builder.setItems(options) { dialog, item ->
            when {
                options[item] == "Take Photo" -> {
                    openCamera()
                }
                options[item] == "Choose From Gallery" -> {
                    pickImages.launch("image/*")     // We want images, so we set the mimeType as "image/*"
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }
}