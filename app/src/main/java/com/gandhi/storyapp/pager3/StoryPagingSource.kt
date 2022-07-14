package com.gandhi.storyapp.pager3

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gandhi.storyapp.retrofit.ApiService
import com.gandhi.storyapp.retrofit.ListStoryItem


class StoryPagingSource (private val apiService: ApiService, private val token: String) : PagingSource<Int, ListStoryItem>()  {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val response = apiService.getAllStoriesPager("Bearer $token", page, params.loadSize)
            val item = response.listStory
            Log.d("searchToken", "load: $item")
            LoadResult.Page(
                data= item,
                prevKey = if (page == 1) null else page + 1,
                nextKey = if (item.isNullOrEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            Log.d("Error", "load: AWJDBALSHBDALUHSD ")
            LoadResult.Error(e)

        }

    }

}