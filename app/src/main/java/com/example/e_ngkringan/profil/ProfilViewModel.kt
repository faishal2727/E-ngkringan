package com.example.e_ngkringan.profilimport androidx.lifecycle.LiveDataimport androidx.lifecycle.MutableLiveDataimport androidx.lifecycle.ViewModelimport com.example.e_ngkringan.api.ApiConfigimport com.example.e_ngkringan.model.ResponseUserCurrentimport retrofit2.Callimport retrofit2.Callbackimport retrofit2.Responseclass ProfilViewModel : ViewModel() {    private val _getUser = MutableLiveData<ResponseUserCurrent>()    val getUser: LiveData<ResponseUserCurrent> = _getUser    private val _isLoading = MutableLiveData<Boolean>()    val isLoading: LiveData<Boolean> = _isLoading    fun getUser(token: String) {        _isLoading.value = true        ApiConfig.getApiService().getUserLogin(token)            .enqueue(object : Callback<ResponseUserCurrent> {                override fun onResponse(                    call: Call<ResponseUserCurrent>,                    response: Response<ResponseUserCurrent>                ) {                    _isLoading.value = false                    if (response.isSuccessful) {                        _getUser.postValue(response.body())                    }                }                override fun onFailure(call: Call<ResponseUserCurrent>, t: Throwable) {                    _isLoading.value = false                }            })    }}