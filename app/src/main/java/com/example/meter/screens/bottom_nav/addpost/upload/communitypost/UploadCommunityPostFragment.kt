package com.example.meter.screens.bottom_nav.addpost.upload.communitypost

import android.Manifest
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var uri: Uri? = null


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

        if (it == true) {

            uri?.let { it1 ->
                photoUriList.add(it1)
                adapter.submitData(photoUriList)
            };
            val file = uri?.toFile(requireContext())
//
            val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
            val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
            photoFileList.add(filePart)
        }
    }


    // Pick Images Contract - Normally this is for all kind of files.
    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { uri ->
                photoUriList.add(uri);
                adapter.submitData(photoUriList)
                val file = uri.toFile(requireContext())
//
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
                    Log.i("degee", "$it")
                }

                Resource.Status.SUCCESS -> {
                    if(photoFileList.isEmpty()){
                        binding.root.findNavController().navigate(R.id.action_uploadCommunityPostFragment_to_singleCommunityPostFragment)
                    }else {
                        it.data?.id?.let { it1 -> viewModel.uploadPhoto(it1, photoFileList) }
                    }
                }

                Resource.Status.LOADING -> {
                    binding.save.isEnabled = false
                    Log.i("debugee", "LOADING")
                }
            }
        })

        viewModel.photoUploaded.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.save.isEnabled = true
                    //TODO rollback savedPost on error
                    Log.i("degee", "$it")
                }

                Resource.Status.SUCCESS -> {
                    binding.save.isEnabled = true
                    Log.i("conentnID", "${it.data}")
                    if(it.data!!){
                        binding.root.findNavController().navigate(R.id.action_uploadCommunityPostFragment_to_singleCommunityPostFragment)
                    }
                }
                Resource.Status.LOADING -> Log.i("debugee", "LOADING")
            }
        })
    }

//    private fun reSizePhoto(uri: Uri,width: Int): Bitmap{
//
//        val rawTakenImage = BitmapFactory.decodeFile(uri.path)
//        return  BitmapScaler.scaleToFitWidth(rawTakenImage, width)
//    }
//
//    private fun writeToDisk(bitmap: Bitmap){
//        // Configure byte output stream
//        val bytes = ByteArrayOutputStream()
//// Compress the image further
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes)
//// Create a new file for the resized bitmap (`getPhotoFileUri` defined above)
//        val resizedFile: File = getPhotoFileUri(photoFileName.toString() + "_resized")
//        resizedFile.createNewFile()
//        val fos = FileOutputStream(resizedFile)
//// Write the bytes of the bitmap to file
//// Write the bytes of the bitmap to file
//        fos.write(bytes.toByteArray())
//        fos.close()
//    }
}