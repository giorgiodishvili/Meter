package com.example.meter.screens.bottom_nav.addpost.upload.carpost.secondpart

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.meter.R
import com.example.meter.adapter.communitypost.upload.UploadCommunityPostPhotoRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.PhotoUploadCarSellFragmentBinding
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.extensions.toBitmap
import com.example.meter.extensions.toFile
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject

@AndroidEntryPoint
class PhotoUploadCarSellFragment :
    BaseFragment<PhotoUploadCarSellFragmentBinding, PhotoUploadCarSellViewModel>(
        PhotoUploadCarSellFragmentBinding::inflate,
        PhotoUploadCarSellViewModel::class.java
    ) {

    private val photoUriList: MutableList<Uri> = mutableListOf()
    private val photoFileList: MutableList<MultipartBody.Part> = mutableListOf()
    private var postId: Long = -1L
    private var latestTmpUri: Uri? = null

    private lateinit var resultCheck: MutableList<String>

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
            if (it[Manifest.permission.READ_EXTERNAL_STORAGE] == true) {
                pickImages.launch("image/*")
            }
        }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                latestTmpUri = uri
                popDialog(R.layout.dialog_item_loading)
                runObjectDetection(uri, requireActivity())
            }
        }

    private fun listeners() {
        val previousStep = arguments?.getParcelable<SellCarPostRequest>("sellPost")

        binding.save.setOnClickListener {
            if (previousStep != null) {
                viewModel.uploadPost(
                    firebaseAuthImpl.getUserId().toString(),
                    binding.descriptionET.text.toString(),
                    previousStep
                )
                observer()
                popDialog(R.layout.dialog_item_uploading)
            }
        }

        adapter.uploadButton = {
            if (hasRead()) {
                pickImages.launch("image/*")
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

    fun runObjectDetection(uri: Uri, context: Context) {
        CoroutineScope(Dispatchers.Default).launch {
            val bitmap = uri.toBitmap(context)
            val image = TensorImage.fromBitmap(bitmap)
            val options = ObjectDetector.ObjectDetectorOptions.builder()
                .setMaxResults(3)
                .setScoreThreshold(0.5f)
                .build()
            val detector = ObjectDetector.createFromFileAndOptions(
                context,
                "model.tflite",
                options
            )

            val results = detector.detect(image)
            results.map {
                val category = it.categories.first()
                val output = listOf<String>("${category.score * 100}", category.label)
                resultCheck = output.toMutableList()
                dialogItem.cancel()
            }
            delay(6000)
            if (!this@PhotoUploadCarSellFragment::resultCheck.isInitialized) {
                resultCheck = mutableListOf("ვერ მოხდა ფოტოს ამოკითხვა")
                results.clear()
                dialogItem.cancel()
            }
            CoroutineScope(Dispatchers.Main).launch {
                if (resultCheck.size == 2) {
                    if (resultCheck[1] == "car" || resultCheck[0] == "truck") {
                        latestTmpUri?.let { uri ->
                            photoUriList.add(uri)
                            adapter.submitData(photoUriList)
                            val file = uri.toFile(requireContext())
                            Log.i("latestempURI", uri.toString())
                            Log.i("filefile", "$file")

                            val photoFile = file!!.asRequestBody(file.extension.toMediaTypeOrNull())
                            val filePart =
                                MultipartBody.Part.createFormData("file", file.name, photoFile)
                            photoFileList.add(filePart)
                        }
                    } else {
                        popDialog(
                            R.layout.dialog_item_error,
                            R.id.errorMsg,
                            "გამოსახულება დიდი ალბათობით ავტომობილი არ არის, სცადეთ ხელახლა"
                        )
                    }
                } else {
                    popDialog(
                        R.layout.dialog_item_error,
                        R.id.errorMsg,
                        "ვერ მორხდა ობიექტის ამოცნობა"
                    )
                }
            }
        }
    }

}