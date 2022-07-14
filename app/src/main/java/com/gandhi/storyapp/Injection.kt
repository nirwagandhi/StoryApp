package com.gandhi.storyapp

import android.content.Context
import com.gandhi.storyapp.pager3.StoryRepository
import com.gandhi.storyapp.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository(apiService)

    }
}