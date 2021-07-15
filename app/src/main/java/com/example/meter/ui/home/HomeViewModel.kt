package com.example.meter.ui.home

import android.util.Log.i
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.AutomobileCategory
import com.example.meter.entity.Model
import com.example.meter.entity.PostItem
import com.example.meter.network.Resource
import com.example.meter.repository.automobile.AutomobileCategoryRepository
import com.example.meter.repository.post.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val categoryRepository: AutomobileCategoryRepository, private val postsRepository: PostRepository) : ViewModel() {
    private val _categories = MutableLiveData<Resource<List<AutomobileCategory>>>()

    val categories: LiveData<Resource<List<AutomobileCategory>>>
        get() = _categories

    private val _make = MutableLiveData<Resource<Model>>()

    val make: LiveData<Resource<Model>>
        get() = _make

    private val _latestPosts = MutableLiveData<Resource<List<PostItem>>>()

    val latestPosts: LiveData<Resource<List<PostItem>>>
        get() = _latestPosts

    fun getAllCategories() =
        viewModelScope.launch {
            _categories.postValue(Resource.loading())
            categoryRepository.getAllManufacturers().let {
                if (it.isSuccessful) {
                    _categories.postValue(Resource.success(it.body()!!))
                } else {
                    _categories.postValue(Resource.error(it.message().toString()))
                }
            }
        }

    fun getModelFromMake(make: String) =
        viewModelScope.launch {
            _make.postValue(Resource.loading())
        categoryRepository.getModelsForMake(make).let {
            if (it.isSuccessful) {
                _make.postValue(Resource.success(it.body()!!))
            } else {
                _make.postValue(Resource.error(it.message().toString()))
            }
        }
    }

    fun getLatestPosts() = viewModelScope.launch {
        _latestPosts.postValue(Resource.loading())
        postsRepository.getLatestPosts().let {
            if (it.isSuccessful) {
                _latestPosts.postValue(Resource.success(it.body()!!))
            } else {
                _latestPosts.postValue(Resource.error(it.message().toString()))
            }
        }
    }



    fun searchCarsForSale(query: String?) {
        i("SearchWord", query!!)
    }
}