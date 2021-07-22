//package com.example.meter.screens.bottom_nav.home
//
//import androidx.lifecycle.ViewModel
//
//class HomeViewModel : ViewModel() {
////    private val _categories = MutableLiveData<Resource<List<AutomobileCategory>>>()
////
////    val categories: LiveData<Resource<List<AutomobileCategory>>>
////        get() = _categories
////
////    private val _make = MutableLiveData<Resource<Model>>()
////
////    val make: LiveData<Resource<Model>>
////        get() = _make
////
////    private val _latestPosts = MutableLiveData<Resource<List<SellCarPost>>>()
////
////    val latestPosts: LiveData<Resource<List<SellCarPost>>>
////        get() = _latestPosts
////
////    fun getAllCategories() =
////        viewModelScope.launch {
////            _categories.postValue(Resource.loading())
////            categoryRepository.getAllManufacturers().let {
////                if (it.isSuccessful) {
////                    _categories.postValue(Resource.success(it.body()!!))
////                } else {
////                    _categories.postValue(Resource.error(it.message().toString()))
////                }
////            }
////        }
////
////    fun getModelFromMake(make: String) =
////        viewModelScope.launch {
////            _make.postValue(Resource.loading())
////        categoryRepository.getModelsForMake(make).let {
////            if (it.isSuccessful) {
////                _make.postValue(Resource.success(it.body()!!))
////            } else {
////                _make.postValue(Resource.error(it.message().toString()))
////            }
////        }
////    }
////
////    fun getLatestPosts() = viewModelScope.launch {
////        _latestPosts.postValue(Resource.loading())
////        postsRepository.getLatestPosts().let {
////            if (it.isSuccessful) {
////                _latestPosts.postValue(Resource.success(it.body()!!))
////            } else {
////                _latestPosts.postValue(Resource.error(it.message().toString()))
////            }
////        }
////    }
////
////
////
////    fun searchCarsForSale(query: String?) {
////        i("SearchWord", query!!)
////    }
//}