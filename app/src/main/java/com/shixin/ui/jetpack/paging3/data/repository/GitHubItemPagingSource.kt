package com.shixin.ui.jetpack.paging3.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.shixin.ui.jetpack.paging3.data.remote.GitHubService
import com.shixin.ui.jetpack.paging3.data.remote.GithubAccountModel
import org.jetbrains.anko.AnkoLogger
import java.lang.Exception

class GitHubItemPagingSource(
        private val api: GitHubService
) : PagingSource<Int, GithubAccountModel>(), AnkoLogger {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubAccountModel> {
        return try {
            val key = params.key ?: 0;
            //获取网络数据
            val items = api.getGithubAccount(key, params.loadSize)
            // 请求失败或者出现异常，会跳转到 case 语句返回 LoadResult.Error(e)
            // 请求成功，构造一个 LoadResult.Page 返回
            LoadResult.Page(
                    data = items, // 返回获取到的数据
                    prevKey = null,// 上一页，设置为空就没有上一页的效果，这需要注意的是，如果是第一页需要返回 null，否则会出现多次请求
                    nextKey = items.lastOrNull()?.id// 下一页，设置为空就没有加载更多效果，需要注意的是，如果是最后一页返回 null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubAccountModel>): Int? {
        return 0
    }

}
