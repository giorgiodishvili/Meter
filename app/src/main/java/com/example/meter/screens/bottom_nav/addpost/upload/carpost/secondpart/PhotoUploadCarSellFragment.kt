package com.example.meter.screens.bottom_nav.addpost.upload.carpost.secondpart

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meter.R
import com.example.meter.adapter.communitypost.upload.UploadCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.PhotoUploadCarSellFragmentBinding
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.extensions.toFile
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class PhotoUploadCarSellFragment : BaseFragment<PhotoUploadCarSellFragmentBinding, PhotoUploadCarSellViewModel>(
    PhotoUploadCarSellFragmentBinding::inflate,
    PhotoUploadCarSellViewModel::class.java
) {

    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val photoFileList: MutableList<MultipartBody.Part> = mutableListOf()
    private var postId: Long = -1L
    private var latestTmpUri: Uri? = null

    private lateinit var adapter: UploadCommunityPostPhotoRecyclerAdapter

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        hideOnToolbar(binding.include3.chatfragments, binding.include3.userProfile)
        initRecycler()
        listeners()
    }

    private fun initRecycler() {

        adapter = UploadCommunityPostPhotoRecyclerAdapter()
        binding.recyclerPhotos.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.recyclerPhotos.adapter = adapter
        preloadedButton()
        adapter.closeButton = {
            photoFileList.removeAt(it - 1)
            photoUriList.removeAt(it - 1)
            adapter.notifyItemRemoved(it)
        }
    }

    val requestPermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (it[Manifest.permission.CAMERA] == true && it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                showDialog()
            }
        }

    private fun listeners() {
        val previousStep = arguments?.getParcelable<SellCarPostRequest>("sellPost")

        binding.save.setOnClickListener {
            if (previousStep != null) {
                viewModel.uploadPost(firebaseAuthImpl.getUserId().toString(), binding.descriptionET.text.toString(), previousStep)
                observer()
                popDialog(R.layout.dialog_item_uploading)
            }
        }

        adapter.uploadButton = {
            if (hasCamera() && hasRead()) {
                showDialog()
            } else {
                requestPermission(requestPermissionResult)
            }
        }
    }

    private fun observer() {

        viewModel.postUploaded.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.ERROR -> {
                    binding.save.isEnabled = true
                }

                Resource.Status.SUCCESS -> {
                    postId = it.data!!.id.toLong()
                    if (photoFileList.isEmpty()) {
                        findNavController()
                            .navigate(
                                R.id.action_global_singleCarSellPostFragment,
                                bundleOf("postId" to postId)
                            )
                        dialogItem.cancel()
                    } else {
                        viewModel.uploadPhoto(
                            postId,
                            photoFileList
                        )
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
                }

                Resource.Status.SUCCESS -> {
                    binding.save.isEnabled = true
                    if (it.data!!) {

                        findNavController()
                            .navigate(
                                R.id.action_global_singleCarSellPostFragment,
                                bundleOf("postId" to postId)
                            )
                        dialogItem.cancel()

                    }
                }
                Resource.Status.LOADING -> {
                    Log.i("debugee", "LOADING")
                }
            }
        })
    }

    private fun preloadedButton() {
        val resources: Resources = requireContext().resources

        val uri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.ic_upload_icon))
            .appendPath(resources.getResourceTypeName(R.drawable.ic_upload_icon))
            .appendPath(resources.getResourceEntryName(R.drawable.ic_upload_icon))
            .build()

        photoUriList.add(uri)
        adapter.submitData(photoUriList)
        adapter.notifyDataSetChanged()
    }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { it ->
                photoUriList.add(it)
                adapter.submitData(photoUriList)
                val file = it.toFile(requireContext())
                Log.i("latestempURI", it.toString())
                Log.i("filefile", "$file")

                val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", file.name, photoFile)
                photoFileList.add(filePart)
            }
        }

    private fun showDialog() {
        if (photoUriList.size < 6) {
            val options = arrayOf<CharSequence>("Take Photo", "Choose From Gallery", "Cancel")
            val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Select Option")
            builder.setItems(options) { dialog, item ->
                when {
                    options[item] == "Take Photo" -> {
                        openCamera()
                    }
                    options[item] == "Choose From Gallery" -> {
                        pickImages.launch("image/*")
                    }
                    options[item] == "Cancel" -> {
                        dialog.dismiss()
                    }
                }
            }
            builder.show()
        } else {
            popDialog(
                R.layout.dialog_item_error,
                R.id.errorMsg,
                "მაქსიმუმ შესაძლებელია 5 ფოტოს ატვირთვა"
            )
        }
    }

    private val takePic = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        if (it == true) {

            latestTmpUri?.let { it1 ->
                photoUriList.add(it1)
                adapter.submitData(photoUriList)

                val file = it1.toFile(requireContext())
                Log.i("latestempURI", it1.toString())
                Log.i("filefile", file.toString())


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

    private fun openCamera() {
        val filename = "photo.jpg"
        val imageUri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        val imagesDetails = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, filename)
        }
        requireActivity().contentResolver.insert(imageUri, imagesDetails).let {
            if (it != null) {
                latestTmpUri = it
                takePic.launch(latestTmpUri)
            }
        }
    }



}