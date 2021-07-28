package com.example.meter.screens.bottom_nav.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.UserDetails
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.network.Resource
import com.example.meter.repository.post.sellpost.CarPostRepository
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val carPostRepository: CarPostRepository,
    private val userInfo: UserInfoRepositoryImpl
) :
    ViewModel() {

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    fun getMarketPosts(): LiveData<PagingData<SellCarPostForMainPage>> {
        return carPostRepository.getSellPostsForMainPage().cachedIn(viewModelScope)
    }

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userInfo.getUserPersonalInfo(uid)
                _readUserInfo.postValue(result)
            }
        }
    }

    fun searchPost(
        manufacturer: String = "",
        model: String = "",
        releaseYearFrom: Int?,
        releaseYearTo: Int?,
        priceFrom: Long?,
        priceTo: Long?


    ): LiveData<PagingData<SellCarPostForMainPage>> {
        val keyword = StringBuilder("")

        if (manufacturer.isNotEmpty()) {
            keyword.appendLine("manufacturer:*$manufacturer* AND")
        }
        if (model.isNotEmpty()) {
            keyword.appendLine("model:*$model* AND")
        }
        if (releaseYearFrom != null) {
            keyword.appendLine("releaseYear>=$releaseYearFrom AND")
        }
        if (releaseYearTo != null) {
            keyword.appendLine("releaseYear<$releaseYearTo AND")
        }
        if (priceFrom != null) {
            keyword.appendLine("price>=$priceFrom AND")
        }
        if (priceTo != null) {
            keyword.appendLine("price<$priceTo AND")
        }
        return carPostRepository.searchPosts(keyword).cachedIn(viewModelScope)
    }
}