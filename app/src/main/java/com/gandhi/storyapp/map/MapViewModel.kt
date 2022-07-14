package com.gandhi.storyapp.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gandhi.storyapp.retrofit.ApiConfig
import com.gandhi.storyapp.retrofit.ListStoryItem
import com.gandhi.storyapp.retrofit.StoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel : ViewModel() {

    private val _showMap = MutableLiveData<List<ListStoryItem>>()
    val showMap: LiveData<List<ListStoryItem>> = _showMap

    companion object {
        const val TAG = "JSON"
    }

    fun showMaps(token: String) {

        val client = ApiConfig.getApiService().getAllLocation("Bearer $token", 1)
        client.enqueue(object : Callback<StoryResponse> {
            override fun onResponse(call: Call<StoryResponse>, response: Response<StoryResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _showMap.value = response.body()?.listStory
                        Log.d(TAG, "onResponse: ${response.body()}")
                    } else {
                        Log.d(TAG, "onFailure : ${response.errorBody()}")

                    }
                }
            }

            override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")

            }

        })
    }
}