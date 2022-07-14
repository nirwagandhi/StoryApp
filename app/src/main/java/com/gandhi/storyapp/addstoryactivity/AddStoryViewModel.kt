package com.gandhi.storyapp.addstoryactivity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.gandhi.storyapp.Event
import com.gandhi.storyapp.model.UserPreference
import com.gandhi.storyapp.retrofit.ApiConfig
import com.gandhi.storyapp.retrofit.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: UserPreference) : ViewModel() {

    private val _addStory = MutableLiveData<Event<String>>()
    val addStory: LiveData<Event<String>> = _addStory

    companion object {
        const val TAG = "uploadStory"
    }

    fun getUser(): LiveData<String> {
        return pref.getUser().asLiveData()
    }

    fun addStory(token: String, imageMultipart: MultipartBody.Part, description: RequestBody) {
        val client =
            ApiConfig.getApiService().uploadStory("Bearer $token", imageMultipart, description)
        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _addStory.value = Event(responseBody.message)
                    }
                } else {
                    _addStory.value = Event(response.message())

                }
            }

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message} ")
            }

        })
    }

}