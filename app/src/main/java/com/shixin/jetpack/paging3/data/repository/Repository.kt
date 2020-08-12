package com.shixin.jetpack.paging3.data.repository

import androidx.paging.PagingData
import com.shixin.bean.GitHubAccount
import kotlinx.coroutines.flow.Flow
import org.jetbrains.anko.AnkoLogger


interface Repository: AnkoLogger {
    fun postOfData(id:Int) :Flow<PagingData<GitHubAccount>>
}