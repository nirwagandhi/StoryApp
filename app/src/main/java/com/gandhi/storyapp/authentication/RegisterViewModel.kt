package com.gandhi.storyapp.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gandhi.storyapp.Event
import com.gandhi.storyapp.retrofit.ApiConfig
import com.gandhi.storyapp.retrofit.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isSuccess = MutableLiveData<Event<String>>()
    val isSuccess: LiveData<Event<String>> = _isSuccess

    fun register(name: String, email: String, password: String) {
        val client = ApiConfig.getApiService().register(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {

                if (response.isSuccessful) {
                    _isSuccess.value = Event("Registration Succes")
                    val responseBody = response.body()
                    if (responseBody != null) {
                        Log.e(RegisterActivity.TAG, "onResponse: ${response.message()} ")

                    }
                } else {
                    Log.e(RegisterActivity.TAG, "onFailure : ${response.errorBody()}")
                    _isSuccess.value = Event("Email has been Taken")


                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(RegisterActivity.TAG, "onFailure : ${t.message}")

            }

        })
    }
}