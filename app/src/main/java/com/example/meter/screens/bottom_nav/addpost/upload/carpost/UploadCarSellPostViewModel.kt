package com.example.meter.screens.bottom_nav.addpost.upload.carpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.repository.post.community.post.CommunityPostRepository
import com.example.meter.repository.post.sellpost.CarPostRepository
import javax.inject.Inject

class UploadCarSellPostViewModel @Inject constructor(private val carPostRepository: CarPostRepository) : ViewModel() {




//
//    private var _outputTf = MutableLiveData<Resource<List<String>>>()
//    val outputTf: LiveData<Resource<List<String>>> = _outputTf
//    private lateinit var resultCheck: MutableList<String>
//
//
//    private var _loading = MutableLiveData<Boolean>().apply { true }
//    var loading: LiveData<Boolean> = _loading
//
//    fun runObjectDetection(bitmap: Bitmap, context: Context) {
//        viewModelScope.launch {
//            withContext(Dispatchers.Default) {
//                val image = TensorImage.fromBitmap(bitmap)
//                val options = ObjectDetector.ObjectDetectorOptions.builder()
//                    .setMaxResults(3)
//                    .setScoreThreshold(0.5f)
//                    .build()
//                val detector = ObjectDetector.createFromFileAndOptions(
//                    context,
//                    "model.tflite",
//                    options
//                )
//
//                val results = detector.detect(image)
//                results.map {
//                    val category = it.categories.first()
//                    val output = listOf<String>("${category.score * 100}", category.label)
//                    resultCheck = output.toMutableList()
//
//                    _outputTf.postValue(Resource.success(output))
//                    _loading.postValue(false)
//
//                    resultCheck.clear()
//                }
//                delay(6000)
//                if (!this@UploadCarSellPostViewModel::resultCheck.isInitialized) {
//                    _outputTf.postValue(Resource.error("can't recognise object"))
//                    _loading.postValue(false)
//                    results.clear()
//                }
//            }
//        }
//    }
}