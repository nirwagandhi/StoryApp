package com.gandhi.storyapp.authentication

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.gandhi.storyapp.model.UserPreference
import com.gandhi.storyapp.retrofit.ApiConfig
import com.gandhi.storyapp.retrofit.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<String> {
        return pref.getUser().asLiveData()
    }


    fun saveUser(user: String) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }


    fun getLogin(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if (response.isSuccessful) {
                    val token = response.body()?.loginResult?.token
                    Log.d(LoginActivity.TAG, "${response.body()?.loginResult?.token}")
                    saveUser(token!!)

                } else {
                    Log.e(LoginActivity.TAG, "onFailure : ${response.errorBody()}")

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d(LoginActivity.TAG, "onFailure: ${t.message}")
            }

        })
    }
}