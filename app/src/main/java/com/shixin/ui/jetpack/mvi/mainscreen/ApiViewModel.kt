package com.shixin.ui.jetpack.mvi.mainscreen

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.shixin.ui.jetpack.mvi.base.BaseViewModel
import com.shixin.ui.jetpack.mvi.entity.ApiResponse
import com.shixin.ui.jetpack.mvi.observer.StateLiveData
import com.shixin.ui.jetpack.mvi.repository.WxArticleRepository
import kotlinx.coroutines.launch

class ApiViewModel : BaseViewModel() {

    private val repository by lazy { WxArticleRepository() }

    val wxArticleLiveData = StateLiveData<List<Any>>()
    val userLiveData = StateLiveData<Any?>()
    private val dbLiveData = StateLiveData<List<Any>>()
    private val apiLiveData = StateLiveData<List<Any>>()
    val mediatorLiveDataLiveData = MediatorLiveData<ApiResponse<List<Any>>>().apply {
        this.addSource(apiLiveData) {
            this.value = it
        }
        this.addSource(dbLiveData) {
            this.value = it
        }
    }

    fun requestNet() {
        viewModelScope.launch {
            wxArticleLiveData.value = repository.fetchWxArticleFromNet()
        }
    }

    fun requestNetError() {
        viewModelScope.launch {
            wxArticleLiveData.value = repository.fetchWxArticleError()
        }
    }

    fun requestFromNet() {
        viewModelScope.launch {
            apiLiveData.value = repository.fetchWxArticleFromNet()
        }
    }

    fun requestFromDb() {
        viewModelScope.launch {
            dbLiveData.value = repository.fetchWxArticleFromDb()
        }
    }

    /**
     * 该请求使用Flow优化，自带loading。
     */
    fun login(username: String, password: String) {
        launchWithLoading(requestBlock = {
            repository.login(username, password)
        }, resultCallback = {
            userLiveData.value = it
        })
    }
}