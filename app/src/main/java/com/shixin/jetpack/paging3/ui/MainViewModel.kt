package com.shixin.jetpack.paging3.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import com.shixin.bean.GitHubAccount
import com.shixin.jetpack.paging3.data.repository.Repository

/**
 * <pre>
 *     author: dhl
 *     date  : 2020/6/20
 *     desc  :
 * </pre>
 */
class MainViewModel(val repository: Repository) :
    ViewModel() {


    /**
     * 我们使用的是 Flow，调用 Flow 的 asLiveData 方法转为 LiveData
     */
    val gitHubLiveData: LiveData<PagingData<GitHubAccount>> =
        repository.postOfData(0).asLiveData()

//        zhihuRepository.postOfZhihuData().asLiveData()

}