package com.gandhi.storyapp.pager3

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.gandhi.storyapp.retrofit.ApiService
import com.gandhi.storyapp.retrofit.ListStoryItem

class StoryRepository(private val apiService: ApiService) {

    fun getStory(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }
}