package com.example.meter.repository.post

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.meter.entity.community.post.Content

interface CommunityPostRepository {
    fun getCommunityPost(): LiveData<PagingData<Content>>

}
