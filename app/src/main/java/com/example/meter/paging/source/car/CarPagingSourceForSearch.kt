package com.example.meter.paging.source.car

import android.util.Log.d
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.community.post.PagedPostResponse
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import retrofit2.HttpException
import java.io.IOException


private const val STARTING_PAGE_INDEX = 0

class CarPagingSourceForSearch(
    private val keyword: String,
    private val apiService: ApiService,
    private val pageSize: Int
) :
    PagingSource<Int, SellCarPostForMainPage>() {
    override fun getRefreshKey(state: PagingState<Int, SellCarPostForMainPage>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SellCarPostForMainPage> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiService.searchCar(keyword, position, pageSize)
            val data = response.body()!!
            d("wesierilogi", "$data")
            LoadResult.Page(
                data = data.content,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                nextKey = if (data.last) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Resource.error<PagedPostResponse<Content>>("Error")
            return LoadResult.Error(exception)
        }
    }
}