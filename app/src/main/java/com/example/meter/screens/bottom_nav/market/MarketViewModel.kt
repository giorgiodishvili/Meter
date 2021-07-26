package com.example.meter.screens.bottom_nav.market

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.repository.post.sellpost.CarPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MarketViewModel @Inject constructor(private val carPostRepository: CarPostRepository) : ViewModel() {

    fun getCommunityPosts(): LiveData<PagingData<SellCarPostForMainPage>> {
        return carPostRepository.getSellPostsForMainPage().cachedIn(viewModelScope)
    }
}