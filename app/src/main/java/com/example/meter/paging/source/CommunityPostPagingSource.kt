package com.example.meter.paging.source

import android.util.Log.i
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.meter.entity.Pageable
import com.example.meter.entity.community.post.CommunityPost
import com.example.meter.entity.community.post.Content
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 0

class CommunityPostPagingSource(private val apiService: ApiService, private val pageSize: Int) : PagingSource<Int, Content>() {
    override fun getRefreshKey(state: PagingState<Int, Content>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Content> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiService.getCommunityPosts(position,params.loadSize)
            val data = response.body()!!
            LoadResult.Page(
                data = data.content,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                nextKey = if (data.last) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Resource.error<CommunityPost>("Error")
            return LoadResult.Error(exception)
        }
    }
}