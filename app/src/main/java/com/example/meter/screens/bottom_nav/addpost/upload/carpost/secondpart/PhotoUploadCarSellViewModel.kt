package com.example.meter.screens.bottom_nav.addpost.upload.carpost.secondpart

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.sell.SellCarPost
import com.example.meter.entity.sell.SellCarPostRequest
import com.example.meter.extensions.toBitmap
import com.example.meter.network.Resource
import com.example.meter.repository.photo.PhotoRepositoryImpl
import com.example.meter.repository.post.sellpost.CarPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector
import javax.inject.Inject

@HiltViewModel
class PhotoUploadCarSellViewModel @Inject constructor(
    private val photoRepository: PhotoRepositoryImpl,
    private val carSellRepository: CarPostRepository,
) : ViewModel() {


    private var _outputTf = MutableLiveData<Resource<List<String>>>()
    val outputTf: LiveData<Resource<List<String>>> = _outputTf

    private lateinit var resultCheck: MutableList<String>

    private var _loading = MutableLiveData<Boolean>().apply { true }
    var loading: LiveData<Boolean> = _loading


    private val _postUploaded = MutableLiveData<Resource<SellCarPost>>()
    val postUploaded: LiveData<Resource<SellCarPost>>
        get() = _postUploaded

    private val _photoUploaded = MutableLiveData<Resource<Boolean>>()
    val photoUploaded: LiveData<Resource<Boolean>>
        get() = _photoUploaded

    fun uploadPost(
        userId: String,
        description: String,
        sellCarPost: SellCarPostRequest
    ) {
        sellCarPost.description = description
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _postUploaded.postValue(
                    carSellRepository.createSellPost(
                        userId,
                        sellCarPost
                    )
                )
            }
        }
    }

    fun uploadPhoto(postId: Long, file: List<MultipartBody.Part>) {
        Log.i("File List ", "$file")

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _photoUploaded.postValue(photoRepository.uploadPhoto(postId, file))
            }
        }
    }

    fun runObjectDetection(uri: Uri, context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
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

                    _outputTf.postValue(Resource.success(output))
                    _loading.postValue(false)

                    resultCheck.clear()
                }
                delay(6000)
                if (!this@PhotoUploadCarSellViewModel::resultCheck.isInitialized) {

                    _outputTf.postValue(Resource.error("can't recognise object"))
                    _loading.postValue(false)
                    results.clear()
                }
            }
        }
    }

}