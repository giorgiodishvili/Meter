package com.example.meter.paging.source.car

import android.util.Log.i
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.meter.entity.community.post.PagedPostResponse
import com.example.meter.entity.sell.SellCarPostForMainPage
import com.example.meter.network.ApiService
import com.example.meter.network.Resource
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 0

class CarPostPagingSource(private val apiService: ApiService, private val networkPageSize: Int) :
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
            val response = apiService.getAllSellPostForMainPage(position, networkPageSize)
            val data = response.body()!!
            i("Car POST", "$data")
            LoadResult.Page(
                data = data.content,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position,
                nextKey = if (data.last) null else position + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Resource.error<PagedPostResponse<SellCarPostForMainPage>>("Error")
            return LoadResult.Error(exception)
        }
    }


}
