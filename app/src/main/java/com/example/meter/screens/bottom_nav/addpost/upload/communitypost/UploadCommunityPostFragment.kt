package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.util.Log.i
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meter.BuildConfig
import com.example.meter.R
import com.example.meter.adapter.UploadCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.UploadCommunityPostFragmentBinding
import com.example.meter.extensions.toFile
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class UploadCommunityPostFragment() :
    BaseFragment<UploadCommunityPostFragmentBinding, UploadCommunityPostViewModel>(
        UploadCommunityPostFragmentBinding::inflate,
        UploadCommunityPostViewModel::class.java
    ) {

    private var latestTmpUri: Uri? = null


    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl
    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val photoFileList: MutableList<MultipartBody.Part> = mutableListOf()
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
            binding.save.isEnabled = false
            postFieldCheck()

        }
    }

    private fun postFieldCheck() {
        if (binding.titleET.text.isNotEmpty() && binding.descriptionET.text.isNotEmpty()) {
            observe()
            firebaseAuthImpl.getUserId()?.let { it1 ->
                viewModel.uploadPost(
                    it1,
                    binding.descriptionET.text.toString(),
                    binding.titleET.text.toString()
                )
            }
        } else {
            binding.save.isEnabled = true
            Toast.makeText(
                requireContext(),
                "Please Fill in all the fields",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    val requestPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA] == true && it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                showDialog()

            }
        }

    private fun getTmpFileUri(): Uri {
        val tmpFile = File.createTempFile(
            "tmp_image_file",
            ".png",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        ).apply {
            createNewFile()
            deleteOnExit()
        }

        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.provider",
            tmpFile
        )
    }

    private fun openCamera() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri
                takePic.launch(uri)
            }
        }
    }

    private val takePic = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        if (it == true) {

            latestTmpUri?.let { it1 ->
                photoUriList.add(it1)
                adapter.submitData(photoUriList)

                val file = it1.toFile(requireContext())
                i("latestempURI", it1.toString())
                i("file", file.toString())


                if (file != null) {
                    val photoFile = file.asRequestBody(file.extension.toMediaTypeOrNull())

                    val filePart =
                        photoFile.let { it2 ->
                            MultipartBody.Part.createFormData(
                                "file", file.name,
                                it2
                            )
                        }

                    photoFileList.add(filePart)
                }
            }
        }
    }


    // Pick Images Contract - Normally this is for all kind of files.
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { it ->
                photoUriList.add(it)
                adapter.submitData(photoUriList)
                val file = it.toFile(requireContext())
                val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
                photoFileList.add(filePart)
            }
        }


    private fun showDialog() {
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


    private fun observe() {
        viewModel.postUploaded.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.save.isEnabled = true
                }

                Resource.Status.SUCCESS -> {
                    if (photoFileList.isEmpty()) {
                        binding.root.findNavController()
                            .navigate(R.id.action_uploadCommunityPostFragment_to_singleCommunityPostFragment)
                    } else {
                        it.data?.id?.let { it1 ->
                            viewModel.uploadPhoto(
                                it1,
                                photoFileList
                            )
                        }
                    }
                }

                Resource.Status.LOADING -> {
                    binding.save.isEnabled = false
                    i("debugee", "LOADING")
                }
            }
        })

        viewModel.photoUploaded.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.save.isEnabled = true
                    //TODO rollback savedPost on error
                }

                Resource.Status.SUCCESS -> {
                    binding.save.isEnabled = true
                    if (it.data!!) {
                        binding.root.findNavController()
                            .navigate(R.id.action_uploadCommunityPostFragment_to_singleCommunityPostFragment)
                    }
                }
                Resource.Status.LOADING -> i("debugee", "LOADING")
            }
        })
    }
}