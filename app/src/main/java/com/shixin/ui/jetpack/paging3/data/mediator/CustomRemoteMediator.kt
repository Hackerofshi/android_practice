package com.shixin.ui.jetpack.paging3.data.mediator

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.shixin.App
import com.shixin.dao.AppDatabase
import com.shixin.ui.jetpack.paging3.data.mapper.Message
import com.shixin.ui.jetpack.paging3.data.remote.GitHubService
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel

@ExperimentalPagingApi
class CustomRemoteMediator(private val api: GitHubService) : RemoteMediator<Int, GithubAccountModel>() {

    private val mMessageDao = AppDatabase.getInstance(App.instance)?.messageDao()
    private var count = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, GithubAccountModel>
    ): MediatorResult {
        val startIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(true)
            LoadType.APPEND -> {
                val stringBuilder = StringBuilder()
                state.pages.forEach {
                    stringBuilder.append("size = ${it.data.size}, count = ${it.data.count()}\n")
                }
                Log.i("pby123", stringBuilder.toString())
                count += state.config.initialLoadSize
                count
            }
        }
        val pages = state.pages
        var totalCount = 0
        if (pages.isNotEmpty()) {
            pages.forEach {
                totalCount += it.data.size + it.itemsBefore + it.itemsAfter
            }
        }

        Log.i("pby123", "CustomRemoteMediator,loadType = $loadType, totalCount = $totalCount")
        return try {
            //  val messages = Service.create().getMessage(state.config.initialLoadSize, startIndex)
            val items = api.getGithubAccount(state.config.initialLoadSize, startIndex)
            AppDatabase.getInstance(App.instance)?.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    mMessageDao?.clearMessage()
                }
                mMessageDao?.insertMessage(items)
            }
            MediatorResult.Success(items.isEmpty())
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

    }
}