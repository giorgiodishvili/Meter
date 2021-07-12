package com.example.meter.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.AutomobileCategory
import com.example.meter.network.Resource
import com.example.meter.repository.AutomobileCategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val categoryRepository: AutomobileCategoryRepository) : ViewModel() {
    private val _categories = MutableLiveData<Resource<List<AutomobileCategory>>>()

    val categories: LiveData<Resource<List<AutomobileCategory>>>
        get() = _categories

    fun getAllCategories() =
        viewModelScope.launch {
            _categories.postValue(Resource.loading())
            categoryRepository.getAllCategories().let {
                if (it.isSuccessful) {
                    _categories.postValue(Resource.success(it.body()!!))
                } else {
                    _categories.postValue(Resource.error(it.message().toString()))
                }
            }
        }
}